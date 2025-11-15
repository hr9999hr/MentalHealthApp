//Creator: Wong Jing Yi (101350)
//Tester: Tan Boon Sang (101173)

package interfaces;

import javax.swing.JPanel;

/**
 * Interface for displaying content visually in a GUI panel.
 */
public interface Displayable {
    /**
     * Returns a JPanel containing the content,
     * fully formatted and ready to be shown on screen.
     *
     * @return JPanel with visual content
     */
    JPanel getDisplayPanel();
}