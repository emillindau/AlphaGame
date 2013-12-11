package controller;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 15:08
 * To change this template use File | Settings | File Templates.
 */
public interface ControlledScreen {

    // Injection of Parent screenPane
    public void setScreenParent(ScreensController screenPage);
}
