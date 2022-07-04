package automationpractice.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import automationpractice.com.pageObject.Account;
import automationpractice.com.pageObject.Cart;
import automationpractice.com.pageObject.CartSummary;
import automationpractice.com.pageObject.Clothes;
import automationpractice.com.pageObject.ShoppingActions;
import automationpractice.com.pageObject.SignInForm;
import utils.EmailsGenerator;

public class ShopWorkflowTest {

	private WebDriver driver;
	private Actions action;

	private Clothes clothes;
	private Cart cart;
	private ShoppingActions shoppingActions;
	private CartSummary summary;
	private SignInForm signinForm;
	private Account account;

	@BeforeClass
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		driver = new ChromeDriver();

		action = new Actions(driver);

		clothes = new Clothes(driver);
		cart = new Cart(driver);
		shoppingActions = new ShoppingActions(driver);
		signinForm = new SignInForm(driver);
		summary = new CartSummary(driver);
		account = new Account(driver);

		String baseUrl = "http://automationpractice.com/index.php?id_category=9&controller=category";
		driver.manage().window().maximize();
		driver.get(baseUrl);
	}

	@AfterClass
	public void closeAll() {
		account.getAccountLogout().click();
		driver.quit();
	}

	@Test(priority = 1)
	public void selectChlotes() {
		Assert.assertTrue(clothes.getDressesBtn().isDisplayed());

		action.moveToElement(clothes.getDressesBtn()).perform();

		Assert.assertTrue(clothes.getSummerDressesBtn().isDisplayed());
		Assert.assertTrue(clothes.getCasualDressesBtn().isDisplayed());
		Assert.assertTrue(clothes.getEveningDressesBtn().isDisplayed());

		// 2. dress
		action.moveToElement(clothes.getDressesBtn()).perform();

		Assert.assertTrue(clothes.getCasualDressesBtn().isDisplayed());

		action.moveToElement(clothes.getCasualDressesBtn()).perform();
		clothes.getCasualDressesBtn().click();
		action.moveToElement(clothes.getCasualDressProduct(1)).perform();
		action.moveToElement(shoppingActions.getAddToCartBtn()).perform();
		action.click(shoppingActions.getAddToCartBtn()).build().perform();

		Assert.assertTrue(shoppingActions.getAddToCartBtn().isDisplayed());

		action.click(shoppingActions.getContinueShopingBtn()).build().perform();
		action.moveToElement(cart.getCartTab()).perform();

		Assert.assertEquals(cart.getCartProductsQty().size(), 1);

	}

	@Test(priority = 4)
	public void checkingCartProductsQtyAndPrice() {
		Assert.assertEquals(cart.getCartProductsQty().size(), 1);

		action.moveToElement(clothes.getDressesBtn()).perform();
		action.moveToElement(clothes.getEveningDressesBtn()).perform();
		action.moveToElement(clothes.getEveningDressProduct(1)).perform();
		action.moveToElement(shoppingActions.getAddToCartBtn()).perform();
		action.click(shoppingActions.getAddToCartBtn()).build().perform();
		action.click(shoppingActions.getContinueShopingBtn()).build().perform();

		action.moveToElement(cart.getCartTab()).perform();
		action.moveToElement(cart.getCartProductsQty(1)).perform();

		Assert.assertEquals(cart.getCartProductsQty(1).getText(), "2");

		action.moveToElement(cart.getCartShipingCost()).perform();

		Assert.assertEquals(cart.getCartShipingCost().getText(), "$2.00");

		action.moveToElement(cart.getCartTotalPrice()).perform();

		Assert.assertEquals(cart.getCartTotalPrice().getText(), "$132.96");
	}

	@Test(priority = 5)
	public void continueToShoppingSummary() {
		action.moveToElement(cart.getCartTab()).perform();
		action.moveToElement(cart.getCartTabCheckOutBtn()).perform();

		Assert.assertTrue(cart.getCartTabCheckOutBtn().isDisplayed());

		action.click(cart.getCartTabCheckOutBtn()).build().perform();
		;

		Assert.assertTrue(summary.getCartSummaryTable().isDisplayed());
		Assert.assertEquals(summary.getCartSummTotalProductsNum().size(), 1);
		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$52.00");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$50.00");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");
		Assert.assertTrue(summary.getCartSummQtyPlus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyPlus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyPlus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyPlus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummDeleteBtn(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummDeleteBtn(2).isDisplayed());
	}

	@Test(priority = 6)
	public void increaseQtyOfProduct1() {
		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$52.00");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$54.00");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");

		summary.getCartSummQtyPlus(1).click();
		driver.navigate().refresh();

		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$78.00");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$80.00");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");
	}

	@Test(priority = 7)
	public void signinRequest() {
		summary.getCartProceedBtn().click();

		Assert.assertTrue(signinForm.getSignInForm().isDisplayed());

		signinForm.setEmailField("email@email.com");
		signinForm.setPasswordField("123456");
		signinForm.getSignInBtn().click();
	}

	@Test(priority = 8)
	public void billingAndDeliveryAddress() {
		Assert.assertEquals(summary.getCartSummBillingAdressName().getText(), "adasd sadasd");
		Assert.assertEquals(summary.getCartSummBillingAdressOne().getText(), "Nulla St. Mankato Mississippi 96522");
		Assert.assertEquals(summary.getCartSummBillingAdressCityState().getText(), "Tokyo, Kentucky 96522");
		Assert.assertEquals(summary.getCartSummBillingAdressCountry().getText(), "United States");
		Assert.assertEquals(summary.getCartSummBillingAdressHomePhone().getText(), "056");
		Assert.assertEquals(summary.getCartSummBillingAdressMobile().getText(), "066");
	}

	@Test(priority = 9)
	public void termsOfServiceModal() {
		summary.getCartProceedBtnTwo().click();
		summary.getCartProceedBtnTwo().click();

		action.moveToElement(summary.getCartSummTermsOfServiceDialog()).perform();

		Assert.assertTrue(summary.getCartSummTermsOfServiceDialog().isDisplayed());

		action.moveToElement(summary.getCartSummTermsOfServiceDialogClose()).perform();
		action.click(summary.getCartSummTermsOfServiceDialogClose()).build().perform();

		driver.navigate().refresh();

		summary.getCartSummTermsOfServiceCheck().click();
		summary.getCartProceedBtnTwo().click();
	}

	@Test(priority = 10)
	public void payment() {
		summary.getCartSummPayByBankWire().click();

		Assert.assertEquals(summary.getCartSummPayByBankWireConfirm().getText(), "BANK-WIRE PAYMENT.");

		summary.getCartSummOtherPaymentMethods().click();
		summary.getCartSummPayByCheck().click();

		Assert.assertEquals(summary.getCartSummPayByCheckConfirm().getText(), "CHECK PAYMENT");
	}

	@Test(priority = 11)
	public void confirmOrder() {
		summary.getCartSummConfirmOrderBtn().click();

		Assert.assertTrue(summary.getCartSummSuccessMsg().isDisplayed());
		Assert.assertEquals(summary.getCartSummSuccessMsg().getText(), "Your order on My Store is complete.");
	}

	@Test(priority = 12)
	public void checkIsOrderVisibleInOrderHistorySection() {
		account.getAccountBtn().click();

		Assert.assertTrue(account.getAccountOrderHistoryBtn().isDisplayed());

		account.getAccountOrderHistoryBtn().click();

		Assert.assertTrue(account.getAccountOrderListTable().isDisplayed());

		account.getAccountBtn().click();
		account.getAccountOrderHistoryBtn().click();

		Assert.assertEquals(account.getAccountOrdersLis().size(), 11);
	}
}
