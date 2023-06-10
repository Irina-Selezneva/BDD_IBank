package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        heading.shouldBe(visible);
    }
    public int getDashboardPage(DataHelper.InfoCard infoCard) {
        var text = cards.findBy(Condition.text(infoCard.getCardNumber().substring(12, 16))).getText();
        return extractBalance(text);
    }
    public TransferPage selectCardToTransfer(DataHelper.InfoCard infoCard) {

        cards.findBy(attribute("data-test-id", infoCard.getCardId())).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}