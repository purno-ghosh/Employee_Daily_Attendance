package Test_Cases;
import Bangladeshi_Employee_Daily_Attendance.Bangladeshi_Employee_Attendance;
import Bangladeshi_Employee_Daily_Attendance.Multiple_Employee_Attendance;
import Setup_All.Setup;
import org.testng.annotations.Test;
import java.io.IOException;

public class All_Test_Cases extends Setup {

    @Test(priority = 1,enabled = true)
    public void Bangladeshi_Employees_Attendance() throws IOException, InterruptedException {
        Bangladeshi_Employee_Attendance actions = new Bangladeshi_Employee_Attendance(driver);
        String Actual_value= actions.SingleEmployeeAttendance();

    }


//    @Test(priority = 2)
//    public void Multiple_Employees_Attendance() throws IOException, InterruptedException {
//        Multiple_Employee_Attendance action2=new Multiple_Employee_Attendance(driver);
//        action2.multipleEmployeeAttendance();
//
//    }


}
