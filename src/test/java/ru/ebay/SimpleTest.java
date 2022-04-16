package ru.ebay;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class SimpleTest {


        @BeforeAll
        static void settingsTest() {
            Configuration.browserSize = "1920x1080";
            Configuration.holdBrowserOpen = true;

        }

        //Так сделал специально, чтобы проверить как работает @DisplayName
        @DisplayName("Предусловия")
        @Test
        void openPage() {

            open("https://www.ebay.com/");          //открыть сайт
            sleep(200);
            $("#gh-eb-Geo").click();                      //установить язык
            $("#gh-eb-Geo-a-en").click();                //установить язык

        }


        @ValueSource(strings = {
                "new balance",
                "addidas"
        })

        @ParameterizedTest(name = "Проверка работоспособности сайта Ebay, ищем: {0}")
        void searchShoesOnEbayTests(String testDataShoes) {

            $("#gh-ac").setValue(testDataShoes).pressEnter();
            sleep(500);
            $(".x-flyout__button").click();
            $(byText("Canada - CAN")).click();
            $("input[value='Go']").click();

            //Проверочки
            $("#gh-shipto-click").shouldBe(Condition.visible);
            $("#x-refine__group_1__0").shouldBe(Condition.visible);
            $("#mainContent").shouldBe(Condition.visible);

        }


        @CsvSource(value = {

                "uniqlo, Women, Kids",
                "zara, Women, Kids"

        })

        @ParameterizedTest(name = "Проверка работоспособности сайта Ebay, ищем: {0}, ожидаем наличие: {1}")
        void checkShoesOnEbayTests(String testDataClothes, String expectedResult) {

            $("#gh-ac").setValue(testDataClothes).pressEnter();

            //проверки
            $$(".srp-refine__category__list").find(Condition.text(expectedResult))
                    .shouldBe(visible);

        }


        static Stream<Arguments> methodSourceExampleTest() {
            return Stream.of(
                    Arguments.of("Zara", List.of(12, 4)),
                    Arguments.of("Uniqlo", List.of(21, 10))
            );

        }

        @MethodSource("methodSourceExampleTest")
        @ParameterizedTest
        void methodSourceExampleTest(String first, List<Integer> second) {
            System.out.println("On our site you searched: " + first + "."
                    + " Amount of searches: " + second + " - things you bought.");
        }


}
