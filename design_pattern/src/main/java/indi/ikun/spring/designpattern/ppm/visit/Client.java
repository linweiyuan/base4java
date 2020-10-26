package indi.ikun.spring.designpattern.ppm.visit;

import indi.ikun.spring.designpattern.ppm.visit.element.*;

public class Client {


    public static void main(String[] args) {

        //获取待初始化数据
        MaterialInit init = new MaterialInit();
        //获取初始化客户端
        VisitorInit visitorInit = new VisitorInit();
        //初始化数据
        System.err.println("系统启动，初始化数据");
        init.accept(visitorInit);

        //获取待分配数据
        MaterialDistribution distribution = new MaterialDistribution();
        VisitorDistribution distribution1=new VisitorDistribution();
        System.err.println("甲方分配物资");
        distribution.accept(distribution1);

        //获取待消费数据
        MaterialConsume consume = new MaterialConsume();
        VisitorConsume consume1 = new VisitorConsume();
        System.err.println("承包商消费物资");
        consume.accept(consume1);

        //获取待清算数据
        MaterialClearing clearing = new MaterialClearing();
        VisitorClearing clearing1 = new VisitorClearing();
        System.err.println("承包商清点物资");
        clearing.accept(clearing1);

        //获取待冻结数据
        MaterialFreeze freeze = new MaterialFreeze();
        VisitorFreeze freeze1 = new VisitorFreeze();
        System.err.println("清点后物资，冻结");
        freeze.accept(freeze1);

        //获取待调拨数据
        MaterialAllocation allocation = new MaterialAllocation();
        VisitorAllocation allocation1 = new VisitorAllocation();
        System.err.println("物资调拨");
        allocation.accept(allocation1);

        //获取待退库数据
        MaterialWithdrawal withdrawal = new MaterialWithdrawal();
        VisitorWithdrawal withdrawal1 = new VisitorWithdrawal();
        System.err.println("物资退库");
        withdrawal.accept(withdrawal1);

    }
}
