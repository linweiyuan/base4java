package org.bougainvillea.java.designpattern.ppm.visit;

import org.bougainvillea.java.designpattern.ppm.visit.element.*;

/**
 * @author renqiankun
 */
public class VisitorWithdrawal extends ParentVisitorPartA {

    @Override
    public void withdrawal(MaterialWithdrawal materialWithdrawal) {
        System.err.println("VisitorWithdrawal withdrawal");
    }

}
