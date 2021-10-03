package com.atypon.apithrottling.functional;

import java.time.LocalDateTime;

public class CircularQueue {

	private LocalDateTime[] queue;
	private int capacity;
	private int front;
	private int rear;
	
	public CircularQueue(int capacity) {
		this.capacity = capacity + 1;
		queue = new LocalDateTime[this.capacity];
		front = 0;
		rear = 0;		
	}
	
	public synchronized boolean enQueue(LocalDateTime element) throws Exception {
		if(isFull()) {
			throw new Exception("Queue is full!!");
		} else {
			queue[rear] = element;
			rear = (rear + 1) % capacity;
			return true;
		}
	}
	
	public synchronized LocalDateTime deQueue() throws Exception {
		if(isEmpty()) {
			throw new Exception("Queue is Empty!!");
		} else {
			LocalDateTime element = queue[front];
			front = (front + 1) % capacity;
			return element;
		}
	}
	
	public int size() {
		int size;
		if(rear > front) {
			size = rear - front;
		} else {
			size = capacity - front + rear;
		}
		
		return size;
	}
	
	public LocalDateTime peekFront() throws Exception {
		if(isEmpty()) {
			throw new Exception("Queue is Empty!!");
		} else {
			return queue[front];
		}
	}
	
	public LocalDateTime peekRear() throws Exception {
		if(isEmpty()) {
			throw new Exception("Queue is Empty!!");
		} else {
			int temp = (rear - 1) % capacity; //Need to do this because rear is always pointing a step ahead
			return queue[temp];
		}
	}
	
	/*
	 * A Circular Queue would be empty when front and rear overlap or point to the same index, i.e., front = rear
	 */
	private boolean isEmpty() {
		if(rear == front) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * A Circular Queue can be full in two cases:
	 * 	- When Rear is pointing at the last element and front is pointing at the first element, so rear - front = capacity - 1
	 * 	- When Rear is right behind front in cases when array is wrapped: rear - front = -1 
	 */
	private boolean isFull() {
		if((rear - front == capacity - 1) || (rear - front == -1)) {
			return true;
		} else {
			return false;
		}
	}
}