package cl.kibernumacademy.inicio.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import cl.kibernumacademy.inicio.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class LoginSteps {

  private WebDriver driver;
  private LoginPage loginPage;

  @Before
  public void setUp() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--headless");
    driver = new ChromeDriver(chromeOptions);
  }

  @After
  public void tearDown() {
    if(driver != null) {
      driver.quit();
    }
  }

  @Given("que el usuario está en la página de login")
  public void que_el_usuario_esta_en_la_pagina_de_login() {
    driver.get("https://bco-selenium.netlify.app/");
    loginPage = new LoginPage(driver);
    tomarSnapshot("snapshot_login_page");
  }

  @When("ingresa usuario {string} y clave {string}")
  public void ingresa_usuario_y_clave(String usuario, String clave) {
    loginPage.login(usuario, clave);
    tomarSnapshot("snapshot_despues_de_login");
  }

  @Then("debería ver el mensaje {string}")
  public void deberia_ver_el_mensaje(String mensaje) {
    String welcomeMessage = loginPage.getWelcomeMessage();
    assertTrue(welcomeMessage.contains(mensaje), "Mensaje esperado: " + mensaje + ", pero se obtuvo: " + welcomeMessage);
    tomarSnapshot("snapshot_welcome");
  }

  private void tomarSnapshot(String nombre) {
    try {
      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      Files.createDirectories(Paths.get("target/screenshots"));
      String fileName = "target/screenshots/" + nombre + ".png";
      Files.copy(screenshot.toPath(), Paths.get(fileName), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

    } catch(IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Error al tomar snapshot: " + e.getMessage());
    }
  }

}
