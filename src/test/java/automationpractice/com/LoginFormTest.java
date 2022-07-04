package automationpractice.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import automationpractice.com.pageObject.Account;
import automationpractice.com.pageObject.CreateAccountForm;
import automationpractice.com.pageObject.SignInForm;
import utils.EmailsGenerator;

public class LoginFormTest {

	private WebDriver driver;
	private CreateAccountForm createAccountForm;
	private SignInForm signin;
	private Account account;

	@BeforeClass
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		driver = new ChromeDriver();

		createAccountForm = new CreateAccountForm(driver);
		signin = new SignInForm(driver);
		account = new Account(driver);

		String baseUrl = "http://automationpractice.com/index.php?controller=authentication";
		driver.manage().window().maximize();
		driver.get(baseUrl);
	}

	@AfterClass
	public void closeAll() {
		account.getAccountLogout().click();
		driver.quit();
	}

	@Test(priority = 1)
	public void loginPage() {
		Assert.assertTrue(signin.getSignInForm().isDisplayed());
		Assert.assertTrue(signin.getSignInEmailField().isDisplayed());
		Assert.assertTrue(signin.getSignInPasswordField().isDisplayed());
		Assert.assertTrue(signin.getSignInBtn().isEnabled());
	}

	@Test(priority = 2)
	public void invalidCredentials() {
		// username: email@email.com
		// password: asddsa

		signin.setEmailField("eduardo@email.com");
		signin.setPasswordField("12345");
		signin.getSignInBtn().click();

		Assert.assertTrue(signin.getAuthenticationFailedError().isDisplayed());

		signin.setEmailField("edu@email.com");
		signin.setPasswordField("12345");
		signin.getSignInBtn().click();

		Assert.assertTrue(signin.getAuthenticationFailedError().isDisplayed());

		signin.setEmailField("email@eduardo.com");
		signin.setPasswordField("12345");
		signin.getSignInBtn().click();

		Assert.assertTrue(signin.getAuthenticationFailedError().isDisplayed());

	}

	@Test(priority = 3)
	public void loginWithoutCredentials() {
		signin.setEmailField("");
		signin.setPasswordField("12345");
		signin.getSignInBtn().click();

		Assert.assertTrue(signin.getEmailRequiredError().isDisplayed());

		signin.setEmailField("eduardo@email.com");
		signin.setPasswordField("");
		signin.getSignInBtn().click();

		Assert.assertTrue(signin.getPasswordRequiredError().isDisplayed());

		signin.setEmailField("");
		signin.setPasswordField("");
		signin.getSignInBtn().click();

		Assert.assertTrue(signin.getEmailRequiredError().isDisplayed());
	}

	@Test(priority = 4)
	public void emailFormat() {
		signin.setEmailField("email");
		signin.getSignInPasswordField().click();

		Assert.assertTrue(signin.getEmailHighlightedRed().isDisplayed());

		signin.setEmailField("edu@email.com");
		signin.getSignInPasswordField().click();

		Assert.assertTrue(signin.getEmailHighlightedRed().isDisplayed());

		signin.setEmailField("email@eduardo.com");
		signin.getSignInPasswordField().click();

		Assert.assertTrue(signin.getEmailHighlightedGreen().isDisplayed());
	}

	@Test(priority = 5)
	public void successfulLogin() {
		signin.setEmailField("email@email.com");
		signin.setPasswordField("123456");
		signin.getSignInBtn().click();

		Assert.assertTrue(createAccountForm.successfullyCreatedAccount().isDisplayed());
	}

}
