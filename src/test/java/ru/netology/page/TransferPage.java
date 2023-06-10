package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement amountInput = $("[data-test-id=amount] input");
    private final SelenideElement fromInput = $("[data-test-id=from] input");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private final SelenideElement errorMessage = $("[data-test-id=error-message]");

    public TransferPage() {
        transferHead.shouldBe(visible);
    }
    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.InfoCard infoCard) {

        makeTransfer(amountToTransfer, infoCard);
        return new DashboardPage();
    }

    public void makeTransfer(String amountToTransfer, DataHelper.InfoCard infoCard) {
        amountInput.setValue(amountToTransfer);
        fromInput.setValue(infoCard.getCardNumber());
        transferButton.click();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }

}
