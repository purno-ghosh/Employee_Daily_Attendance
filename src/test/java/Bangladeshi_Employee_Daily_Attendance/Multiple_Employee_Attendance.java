package Bangladeshi_Employee_Daily_Attendance;

import Setup_All.Setup;
import Setup_All.Utils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Multiple_Employee_Attendance {
    private WebDriver driver;
    private Setup setup;

    @FindBy(xpath = "//input[@name=\"email\"]")
    public WebElement email_field;
    @FindBy(xpath = "//input[@name=\"password\"]")
    public WebElement password_field;
    @FindBy(xpath = "//span[text()=\" Login \"]")
    public WebElement login_click;
    @FindBy(xpath = "//p[@class='day mb-4']")
    public WebElement wedDate;
    @FindBy(xpath = "(//td[contains(@class, 'cdk-column-StartTime')])[1]//span/span/span")
    public WebElement WebClockInTime;
    @FindBy(xpath = "//button[contains(text(),'Clock In')]")
    public WebElement Clock_In;
    @FindBy(xpath = "//button[contains(@class, 'user-button') and contains(@class, 'mat-menu-trigger')]")
    public WebElement Click_Profile;
    @FindBy(xpath = "//button[contains(@class, 'mat-menu-item') and span[text()='Logout']]")
    public WebElement Click_Logout;

    public Multiple_Employee_Attendance(WebDriver driver) {
        this.driver = driver;
        this.setup = new Setup();
        PageFactory.initElements(driver, this);
    }




    public void multipleEmployeeAttendance() throws InterruptedException {
        String emails = Setup.getConfigData("multipleUser");
        String password = Setup.getConfigData("password");
        String[] emailList = emails.split(",");

        try (BufferedWriter clearWriter = new BufferedWriter(new FileWriter("attendance_results.txt"))) {
            clearWriter.write("");
        } catch (IOException e) {
            System.err.println("Error clearing the file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("attendance_results.txt", true))) {
            for (String email : emailList) {
                email = email.trim(); // Remove leading/trailing spaces
                System.out.println("\nRunning test for email: " + email);
                boolean attendanceSuccess = false;

                try {
                    email_field.clear();
                    email_field.sendKeys(email);
                    password_field.sendKeys(password);
                    login_click.click();
                    Thread.sleep(3000);
                    driver.navigate().refresh();

                    String pcDate = Utils.getPcDate();
                    String pcTime = Utils.getPcTime();
                    String Webdate = wedDate.getText();

                    if (Webdate.equals(pcDate)) {
                        System.out.println("Test 01 >> PC and Web dates match.");
                    } else {
                        System.out.println("Test 01 >> PC and Web dates do NOT match.");
                    }

                    try {
                        if (Clock_In.isDisplayed()) {
                            Clock_In.click();
                            System.out.println("Test 02 >> Clock_In button clicked.");
                            attendanceSuccess = true;
                        } else {
                            System.err.println("Clock_In button is not displayed.");
                        }
                    } catch (NoSuchElementException e) {
                        System.err.println("Clock_In button not found.");
                    }

                    driver.navigate().refresh();
                    try {
                        String clockInTime = WebClockInTime.getText();
                        String webClockInTime = clockInTime.substring(0, 5);
                        System.out.println("Web Clock-In Time: " + webClockInTime);
                        System.out.println("PC Clock-In Time: " + pcTime);

                        if (pcTime.equals(webClockInTime)) {
                            System.out.println("Test 03 >> Times are the same.");
                        } else {
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            Date pcTimeDate = format.parse(pcTime);
                            Date webTimeDate = format.parse(webClockInTime);

                            long differenceInMillis = Math.abs(pcTimeDate.getTime() - webTimeDate.getTime());
                            long differenceInMinutes = differenceInMillis / (60 * 1000);

                            if (differenceInMinutes >= 1 && differenceInMinutes <= 5) {
                                System.out.println("Test 03 >> Times are within 1 to 5 minutes difference.");
                            } else {
                                System.out.println("Test 03 >> Time does not match.");
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error comparing times: " + e.getMessage());
                    }

                } catch (Exception e) {
                    System.err.println("Error processing email " + email + ": " + e.getMessage());
                }


                if (attendanceSuccess) {
                    writer.write(email + " >>   Attendance done\n");
                } else {
                    writer.write(email + " >>   Failed to attendance\n");
                }

                try {
                    Click_Profile.click();
                    Click_Logout.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.err.println("Error during logout for " + email + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


}
