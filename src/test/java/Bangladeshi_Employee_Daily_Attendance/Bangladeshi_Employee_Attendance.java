package Bangladeshi_Employee_Daily_Attendance;
import Setup_All.Setup;
import Setup_All.Utils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import java.text.SimpleDateFormat;
import java.util.Date;


 public class Bangladeshi_Employee_Attendance {
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


    public Bangladeshi_Employee_Attendance(WebDriver driver) {
        this.driver = driver;
        this.setup = new Setup();
        PageFactory.initElements(driver, this);
    }

    public String SingleEmployeeAttendance() throws InterruptedException {
        String email = Setup.getConfigData("userEmail");
        String password = Setup.getConfigData("password");
        email_field.sendKeys(email);
        password_field.sendKeys(password);
        login_click.click();

        String pcDate = Utils.getPcDate();
        String pcTime = Utils.getPcTime();
        String Webdate = wedDate.getText();

        if (Webdate.equals(pcDate)) {
            System.out.println("Test 01 >> Pc local date and web local is matching");

        } else {
            System.out.println("Pc local date and web local is NOT  matching");
        }
        Thread.sleep(3000);

        try {
            if (Clock_In.isDisplayed()) {
                Clock_In.click();
                System.out.println("Test 02 >> Clock_In button clicked.");
            } else {
                System.err.println("Clock_In button is not displayed.");
            }
        } catch (NoSuchElementException e) {
            System.err.println("Clock_In button not found.");
            Assert.fail("Clock_In button was not found, test failed.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            Assert.fail("An error occurred: " + e.getMessage());
        }


        driver.navigate().refresh();
        String clockInTIme = WebClockInTime.getText();
        String webclockInTIme = clockInTIme.substring(0, 5);
        System.out.println("Webtime " + webclockInTIme);


        if (pcTime.equals(webclockInTIme)) {
            System.out.println("Test 03 >> Times are the same.");
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date pcDate1 = format.parse(pcTime);
                Date webDate = format.parse(webclockInTIme);

                long differenceInMillis = Math.abs(pcDate1.getTime() - webDate.getTime());
                long differenceInMinutes = differenceInMillis / (60 * 1000);

                if (differenceInMinutes >= 1 && differenceInMinutes <= 5) {
                    System.out.println(" Test 03 >> Times are within 1 to 5 minutes difference.");
                } else {
                    System.out.println("Time not matching.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }


}
