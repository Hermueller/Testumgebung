package at.htl.timemonitoring.common.fx;

import javafx.animation.*;
import javafx.scene.Node;

/**
 * @timeline .
 * 31.03.2016: MET 001  created class
 * 31.03.2016: MET 010  implemented method for blinking text
 * 31.03.2016: MET 005  implemented method for disappearing text
 */
public class TextAnimation {

    public static Timeline createBlinker(Node node) {
        Timeline blink = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.seconds(0),
                        new KeyValue(
                                node.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        javafx.util.Duration.seconds(0.5),
                        new KeyValue(
                                node.opacityProperty(),
                                0,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        javafx.util.Duration.seconds(1),
                        new KeyValue(
                                node.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                )
        );
        blink.setCycleCount(10);
        return blink;
    }

    public static FadeTransition createFade(Node node) {
        FadeTransition fade = new FadeTransition(javafx.util.Duration.seconds(2), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        return fade;
    }

}
