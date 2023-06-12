package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class TransferTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromFirstToSecond() {
        var firstCardInfo = getFirstInfoCard();
        var secondCardInfo = getSecondInfoCard();
        var firstCardBalance = dashboardPage.getDashboardPage(firstCardInfo);
        var secondCardBalance = dashboardPage.getDashboardPage(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getDashboardPage(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getDashboardPage(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferFromSecondToFirst() {

        var firstCardInfo = getSecondInfoCard();
        var secondCardInfo = getFirstInfoCard();
        var firstCardBalance = dashboardPage.getDashboardPage(secondCardInfo);
        var secondCardBalance = dashboardPage.getDashboardPage(firstCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
        var actualBalanceFirstCard = dashboardPage.getDashboardPage(secondCardInfo);
        var actualBalanceSecondCard = dashboardPage.getDashboardPage(firstCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferSumMoreThanValidAmount() {

        var firstCardInfo = getFirstInfoCard();
        var secondCardInfo = getSecondInfoCard();
        var firstCardBalance = dashboardPage.getDashboardPage(firstCardInfo);
        var secondCardBalance = dashboardPage.getDashboardPage(secondCardInfo);
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.findErrorMessage("Недостаточно средств на карте списания");
        var actualBalanceFirstCard = dashboardPage.getDashboardPage(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getDashboardPage(secondCardInfo);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }
}

