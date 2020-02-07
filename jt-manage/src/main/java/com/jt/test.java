package com.jt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class test {

	public static void main(String[] args) {

		thread t = new thread();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				for (int i=1;i<40;i++){
//					t.deleteNum();
//				}
//
//			}
//		},"a").start();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				for (int i=1;i<40;i++){
//					t.deleteNum();
//				}
//
//			}
//		},"b").start();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				for (int i=1;i<40;i++){
//					t.deleteNum();
//				}
//
//			}
//		},"c").start();

		new Thread(() -> {for (int i=1;i<40;i++) t.deleteNum(); },"a").start();
		new Thread(() -> {for (int i=1;i<40;i++) t.deleteNum(); },"b").start();
		new Thread(() -> {for (int i=1;i<40;i++) t.deleteNum(); },"c").start();
	}
}



class thread{
	private int num = 30;
	private Lock lock = new ReentrantLock();
	public void deleteNum(){

		lock.lock();
		try {

			if(num>0) {
				System.out.println(Thread.currentThread().getName() + "当前票数：" + (num--) + "剩余票数：" + num);
			}

		}finally {

			lock.unlock();

		}


	}
}