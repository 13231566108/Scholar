package com.jt;


import java.sql.SQLOutput;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class a{
	private int num = 1 ;// 1:A 2:B 3:C
	private Lock lock = new ReentrantLock();
	private Condition condition1 = lock.newCondition();
	private Condition condition2 = lock.newCondition();
	private Condition condition3 = lock.newCondition();
	public void print5(){
		lock.lock();
		try {
			while (num != 1){
				condition1.await();
			}

			for(int i = 1;i<=5;i++){
				System.out.println(Thread.currentThread().getName()+"  "+i);
			}

			num = 2;
			condition2.signal();

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public void print10(){
		lock.lock();
		try {
			while (num != 2){
				condition2.await();
			}

			for(int i = 1;i<=10;i++){
				System.out.println(Thread.currentThread().getName()+"  "+i);
			}

			num = 3;
			condition3.signal();

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public void print15(){
		lock.lock();
		try {
			while (num != 3){
				condition3.await();
			}

			for(int i = 1;i<=15;i++){
				System.out.println(Thread.currentThread().getName()+"  "+i);
			}

			num = 1;
			condition1.signal();

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
}
public class Test3 {
	public static void main(String[] args) {
		a s = new a();
		new Thread(() -> {
			for(int i=1;i<=10;i++){
				s.print5();
			}
		},"A").start();

		new Thread(() -> {
			for(int i=1;i<=10;i++){
				s.print10();
			}
		},"B").start();

		new Thread(() -> {
			for(int i=1;i<=10;i++){
				s.print15();
			}
		},"C").start();
	}
}
