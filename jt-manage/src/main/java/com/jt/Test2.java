package com.jt;

import com.baomidou.mybatisplus.extension.api.R;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *  高内聚低耦合前提下，线程 操作 资源类
 *  判断 干活 通知
 *  多线程交互中，必须要防止多线程的虚假唤醒，也既（判断只用while ，不能用if）
 */
class AddAndDlete {
	private int number = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public  void Add() {
		lock.lock();
		try {
			while (number != 0) {
				condition.await();
			}

			number++;
			System.out.println(Thread.currentThread().getName() + number);
			condition.signalAll();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public  void delete() throws InterruptedException {
		lock.lock();
		try {
			while (number == 0) {
				condition.await();
			}

			number--;
			System.out.println(Thread.currentThread().getName() + number);
			condition.signalAll();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/*public synchronized void Add() throws InterruptedException {
		while (number != 0){
			this.wait();
		}

		number++;
		System.out.println(Thread.currentThread().getName()+number);
		this.notifyAll();
	}

	public synchronized void delete() throws InterruptedException {
		while (number == 0){
			this.wait();
		}

		number--;
		System.out.println(Thread.currentThread().getName()+number);
		this.notifyAll();
	}
}*/

}


public class Test2 {
	public static void main(String[] args) {
		AddAndDlete addAndDlete = new AddAndDlete();
		new Thread(() -> {
			for (int i = 1 ;i < 10;i++){
				try {
					addAndDlete.Add();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		},"a").start();
		new Thread(() -> {
			for (int i = 1 ;i < 10;i++){
				try {
					addAndDlete.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		},"b").start();

		new Thread(() -> {
			for (int i = 1 ;i < 10;i++){
				try {
					addAndDlete.Add();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		},"c").start();

		new Thread(() -> {
			for (int i = 1 ;i < 10;i++){
				try {
					addAndDlete.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		},"d").start();
	}
}
