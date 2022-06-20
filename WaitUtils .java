package com.prahs.utils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.prahs.BasePages.BaseFragment;


public class WaitUtils {

	protected static Logger log = LoggerFactory.getLogger(BaseFragment.class.getSimpleName());
	protected WebDriver driver;
	protected long waitTimeout = 30;

	public WaitUtils() {
		super();
	}

	protected void waitForPageLoaded() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(waitTimeout))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(ScriptTimeoutException.class)
				.ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
						.equals("complete");
			}
		});
	}
	
	public void waitforTitleLoaded(String title) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.titleContains(title));
		} catch (TimeoutException e) {
			// ok to ignore
		}
	}

	/**
	 * Function to verify whether element is clickable
	 */
	protected void waitUntilClickable(WebElement webElement) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		try {
			log.info("waitUntilClickable " + getDescription(webElement));
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			wait.until(ExpectedConditions.textToBePresentInElement(webElement, webElement.getText()));
		} catch (StaleElementReferenceException e) {
			log.warn(webElement.getText() + " is not Clickable.");
		}

	}

	private String getDescription(WebElement webElement) {
		return TestUtils.getDescription(webElement);
	}

	/**
	 * Function to verify whether element is Visible
	 */
	protected void waitUntilElementVisible(WebElement webElement) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, 60);

			log.info("waitUntilElementVisible {}", getDescription(webElement));
			wait.until(ExpectedConditions.visibilityOf(webElement));
		} catch (Exception e) {
			log.info(webElement.getText() + " is not Visible.");
			log.warn(e.getMessage());
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected void waitUntilElementPresent(By by) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, 45);
			log.info("waitUntilElementVisible {}", by.toString());
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			log.info(by.toString() + " is not Present.");
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected void waitUntilSpinnerProgressComplete(By by) {
		waitUntilAjaxStartsAndCompletes();
		waitForElementToBeInvisible(driver, by);
	}

	protected boolean waitForElementToBeInvisible(WebDriver driver, final By by) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			log.info("waiting for the element " + by.toString() + " to be invisible");
			WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
			boolean present = wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class)
					.ignoring(WebDriverException.class).until(ExpectedConditions.invisibilityOfElementLocated(by));

			return present;
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected void waitUntilNoStaleErrorForElement(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (StaleElementReferenceException e) {
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
		}
	}

	protected void waitUntilNoStaleErrorForElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
		wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
	}
	
	protected void waitandgetelement(WebElement element) {
		new WebDriverWait(driver, waitTimeout)
        .ignoring(StaleElementReferenceException.class)
        .until((WebDriver d) -> {
            return element;
        });
	}

	protected WaitUtils waitIsElementVisible(WebElement element) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
			wait.until(ExpectedConditions.visibilityOf(element));
			return this;
		} catch (StaleElementReferenceException sex) {
			retryFindingElement(element);
			return this;
		} catch (Exception tex) {
			return this;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected WaitUtils waitIsElementVisible(By by) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return this;
		} catch (StaleElementReferenceException sex) {
			retryFindingElement(driver.findElement(by));
			return this;
		} catch (Exception nex) {
			return this;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected WaitUtils waitIsElementsVisible(By by) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
			return this;
		} catch (StaleElementReferenceException sex) {
			retryFindingElement(driver.findElement(by));
			return this;
		} catch (Exception nex) {
			return this;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected WaitUtils waitIsElementsVisible(List<WebElement> wes) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
			wait.until(ExpectedConditions.visibilityOfAllElements(wes));
			return this;
		} catch (Exception nex) {
			return this;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected WebElement retryFindingElement(WebElement we) {
		int attempts = 0;
		while (attempts < 2) {
			try {
				we.isDisplayed();
				break;
			} catch (StaleElementReferenceException e) {
			}
			attempts++;
		}
		return we;
	}

	protected void waitUntilElementNotVisible(WebElement element) {
		log.info("Executing waitUntilElementNotVisible");
		try {
			log.info("wait for Element Displayed method - Waiting until%s - Present", element);
			new WebDriverWait(driver, waitTimeout).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.until(d -> !isElementPresent(element));
		} catch (TimeoutException e) {
			log.info("Unable to find Element wait for Element Not Displayed method by Timeout %s - absent", element);
		} catch (WebDriverException e) {
			log.info("Unable to find Element  wait for Element Not Displayed method %s - absent", element);
		}
	}

	protected void waitUntilElementNotVisible(By by, long customwait) {
		log.info("Executing waitUntilElementNotVisible with long time out "+customwait);
		try {
			log.info("wait for Element Displayed method - Waiting until%s - Present", by);
			new WebDriverWait(driver, customwait).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.until(d -> !isElementPresent(by));
		} catch (TimeoutException e) {
			log.info("Unable to find Element wait for Element Not Displayed method by Timeout %s - absent", by);
		} catch (WebDriverException e) {
			log.info("Unable to find Element  wait for Element Not Displayed method %s - absent", by);
		}
	}
	
	protected boolean isElementPresent(WebElement element) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOf(element));
//			log.info("Is Element Present method - Waiting until" + element.toString() + " - Present");
			return true;
		} catch (NoSuchElementException noe) {
			return false;
		} catch (NullPointerException nex) {
			return false;
		} catch (TimeoutException tex) {
			return false;
		} catch (StaleElementReferenceException ser) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}

	}


	protected void waitUntilAjaxStartsAndCompletes() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		final long MAX_WAIT = 4000;
		while (!isAjaxExecuting()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				log.debug(e.getMessage());
			}
			if (stopWatch.getTime() > MAX_WAIT) {
				log.warn("Ajax call not made, continuing");
				break;
			}
		}
		while (isAjaxExecuting()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				log.debug(e.getMessage());
			}
		}
	}

	protected boolean isElementPresent(By by) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected void waitUntilAjaxComplete() {
		log.info("waitUntilAjaxComplete");
		while (isAjaxExecuting()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				log.debug(e.getMessage());
			}
		}
	}

	private boolean isAjaxExecuting() {
		try {
			Long ajaxCounter = (Long) executeJavaScript("return ajaxCounter;");
			boolean isExecuting = ajaxCounter != null && ajaxCounter > 0;
			log.debug("ajax executing {}", isExecuting);
			return isExecuting;

		} catch (WebDriverException e) {
			return false;
		}
	}

	private Object executeJavaScript(String script) {
		log.debug("execute Javascript {}", script);
		return ((JavascriptExecutor) driver).executeScript(script);
	}
	
	// window.setTimeout executes only when browser is idle,
	 // introduces needed wait time when javascript is
	 // running in browser
	protected void waitUntilJavascriptExecuted() {
		ExpectedCondition<Boolean> javascriptDone = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				try {
					return ((Boolean) ((JavascriptExecutor) d)
							.executeAsyncScript(" var callback =arguments[arguments.length - 1]; " + " var count=42; "
									+ " setTimeout( collect, 0);" + " function collect() { " + " if(count-->0) { "
									+ " setTimeout( collect, 0); " + " } " + " else {callback(" + "    true" + " );}"
									+ " } "));
				} catch (Exception e) {
					return Boolean.FALSE;
				}
			}
		};
		try {
			WebDriverWait w = new WebDriverWait(driver, 1);
			w.until(javascriptDone);
			w = null;
		} catch (TimeoutException tex) {
			log.info("Waiting until the Java Script execution is done");
		}
	}
	
	protected WaitUtils waitIsElementClickable(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTimeout);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			// ok to ignore
		}
		return this;
	}
	
	protected WaitUtils waitIsElementVisible(WebElement element, long timeout) {
		log.info("wait is element visible");
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			FluentWait<WebDriver> wait = new WebDriverWait(driver, timeout)
					.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.visibilityOf(element));
			return this;
		} catch (NoSuchElementException noe) {
			return this;
		} catch (NullPointerException nex) {
			return this;
		} catch (TimeoutException nex) {
			return this;
		} catch (Exception nex) {
			return this;
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}
	
	protected void waitForAjax() {
		driver.manage().timeouts().setScriptTimeout(50, TimeUnit.SECONDS);
		log.info("wait For Ajax");
		((JavascriptExecutor) driver).executeAsyncScript("var callback = arguments[arguments.length - 1];"
				+ "var xhr = new XMLHttpRequest();" + "xhr.open('POST', '/Ajax_call', true);"
				+ "xhr.onreadystatechange = function() {" + "  if (xhr.readyState == 4) {"
				+ "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
	}
	
	public void waitForElementNotDisplayed(WebElement element, long timeout) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		log.info("Executing waitForElementNotDisplayed with long time out");
		try {
			new WebDriverWait(driver, timeout).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return !isElementPresent(element);
						}
					});
		} catch (TimeoutException e) {
			log.info("Element stil present on the page wait for Element Not Displayed method " + element.toString()
					+ " - present Time out");
			e.printStackTrace();
		} catch (WebDriverException e) {
			log.info("Element stil present on the page wait for Element Not Displayed method " + element.toString()
					+ " - present webdriver");
			e.printStackTrace();
		} finally {
			driver.manage().timeouts().implicitlyWait(waitTimeout, TimeUnit.SECONDS);
		}
	}

	protected void waitForFileDownloads(int i) {
		log.info("Waiting for Fixed Time Frame (" + i + ") for File Downloads");
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForEmail() {
		log.info("Waiting for Fixed Time Frame (10000) for Email Arrival");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@FindBy(xpath = "//div[contains(@class,'forceToastMessage') and @role='alert']")
	private WebElement message;

	public void waitForMessage() {
		log.info("Executing waitForMessage");
		try {
			new FluentWait<WebElement>(message).withTimeout(Duration.ofSeconds(waitTimeout))
					.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class).until(new Function<WebElement, Boolean>() {
						public Boolean apply(WebElement element) {
							return isElementPresent(message);
						}
					});
		} catch (TimeoutException e) {
			log.info("Unable to find Message   - absent");
		} catch (WebDriverException e) {
			log.info("Unable to find Message  wait for Message Displayed method  - absent");
		}
	}

	public void waitForMessageDisappear() {
		log.info("Executing waitForMessageDisappear");
		waitForElementNotDisplayed(message);
	}
	
	public void waitForElementNotDisplayed(WebElement element) {
		log.info("Executing waitForElementNotDisplayed");
		try {
			new FluentWait<WebElement>(element).withTimeout(Duration.ofSeconds(waitTimeout))
					.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class)
					.ignoring(WebDriverException.class).ignoring(StaleElementReferenceException.class)
					.until(new Function<WebElement, Boolean>() {
						public Boolean apply(WebElement element) {
							return !isElementPresent(element);
						}
					});
		} catch (TimeoutException e) {
			log.info("Element stil present on the page wait for Element Not Displayed method " + element.toString()
					+ " - present Time out");
		} catch (WebDriverException e) {
			log.info("Element stil present on the page wait for Element Not Displayed method " + element.toString()
					+ " - present webdriver");
			e.printStackTrace();
		}
	}
	
	public void waitForElementsReady() {
		log.info("Executing waitForElementsReady");
		WebDriverWait waitFinishedInit = new WebDriverWait(driver, 30);
		waitFinishedInit.ignoring(StaleElementReferenceException.class)
				.withMessage("Elements are not loaded or it are not present in the page")
				.until(new Function<WebDriver, Boolean>() {
					@Override
					public Boolean apply(WebDriver input) {
						return isElementsUpdated();
					}
				});
	}

	public boolean isElementsUpdated() {
		Object status;
		status = ((JavascriptExecutor) driver)
				.executeScript("return window.$A ? window.$A.finishedInit === true : false;");
		if (status instanceof String && status.equals("ok")) {
			return false;
		}
		return ((Boolean) status).booleanValue();
	}
	
	@FindBy(xpath = ".//div[contains(@class,'oneLoading') and not(contains(@class,'Hide'))]")
	private WebElement loadingIndicator;

	@FindBy(xpath = "//div[contains(@class,'spinner_container') and not (contains(@class,'hide'))]")
	private WebElement modalSpinner;

	public void waitForLoadingIndicatorToDisappear() {
		log.info("Waiting for loading indicator to disappear");
		waitForElementNotDisplayed(loadingIndicator);
	}

	public void waitForModalSpinnerToDisappear() {
		log.info("Waiting for modal spinner to disappear");
		waitForElementNotDisplayed(modalSpinner);
	}
	
	public void waitForElementDisplayed(WebElement element, long timeout) {
		log.info("Executing waitForElementDisplayed with long time out");
		try {
			new WebDriverWait(driver, timeout).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.ignoring(TimeoutException.class).until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return isElementPresent(element);
						}
					});
		} catch (TimeoutException e) {
			log.info("Unable to find Element wait for Element Displayed method TimeoutException - absent");
		} catch (WebDriverException e) {
			log.info("Unable to find Element wait for Element Displayed method WebDriverException - absent");
		}
	}

	protected void waitForElementDisplayed(WebElement element) {
		log.info("Executing waitForElementDisplayed with long time out");
		try {
			new WebDriverWait(driver, waitTimeout).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.ignoring(TimeoutException.class).until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return isElementPresent(element);
						}
					});
		} catch (TimeoutException e) {
			log.info("Unable to find Element wait for Element Displayed method TimeoutException - absent");
		} catch (WebDriverException e) {
			log.info("Unable to find Element wait for Element Displayed method WebDriverException - absent");
		}
	}

	public void waitForElementDisplayed(String elementXpath, long timeout) {
		log.info("Executing waitForElementDisplayed with long time out");
		try {
			log.info("wait for Element Displayed method - Waiting until {}", elementXpath);
			new WebDriverWait(driver, timeout).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return isElementPresent(driver.findElement(By.xpath(elementXpath)));
						}
					});
		} catch (TimeoutException e) {
			log.info("Unable to find Element  wait for Element Displayed method {} - absent", elementXpath);
		} catch (WebDriverException e) {
			log.info("Unable to find Element  wait for Element Displayed method {} - absent", elementXpath);
		}
	}
	
	/**
	 * @description HardCoded wait 
	 * @param milliseconds
	 */
	public static void waitfor(long milliseconds) {
		try {
			log.info("THREAD SLEEP - wait for {} milliseconds", milliseconds);
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
}