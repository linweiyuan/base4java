package indi.ikun.spring.designpattern.ppm.visit;

import indi.ikun.spring.designpattern.ppm.visit.element.*;

/**
 * @author renqiankun
 */
public class VisitorWithdrawal extends Visitor{

    @Override
    public void withdrawal(MaterialWithdrawal materialWithdrawal) {
        System.err.println("VisitorWithdrawal withdrawal");
    }

}
