package indi.ikun.spring.designpattern.ppm.visit;

import indi.ikun.spring.designpattern.ppm.visit.element.*;

/**
 * @author renqiankun
 */
public class VisitorInit extends ParentVisitorPartA {
    @Override
    public void init(MaterialInit materialInit) {
        System.err.println("VisitorInit init");
    }
}
