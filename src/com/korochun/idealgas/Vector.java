package com.korochun.idealgas;

class Vector implements Cloneable {
	
	private double x, y;
	
	public Vector() {
		this(0, 0, false);
	}
	public Vector(double x, double y) {
		this(x, y, false);
	}
	public Vector(double a, double b, boolean rad) {
		if (rad) {
			x = a * Math.cos(b);
			y = a * Math.sin(b);
		} else {
			x = a;
			y = b;
		}
	}
	
	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y);
	}
	@Override
	public Vector clone() {
		return new Vector(x, y);
	}
	public Vector div(double other) {
		return new Vector(x / other, y / other);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double magSq() {
		return x * x + y * y;
	}
	public double mult(Vector other) {
		return x * other.x + y * other.y;
	}
	public Vector mult(double other) {
		return new Vector(x * other, y * other);
	}
	public Vector neg() {
		return new Vector(-x, -y);
	}
	public Vector setX(double vx) {
		return new Vector(vx, y);
	}
	public Vector setY(double vy) {
		return new Vector(x, vy);
	}
	public Vector sub(Vector other) {
		return new Vector(x - other.x, y - other.y);
	}
	public double mag() {
		// TODO Auto-generated method stub
		return Math.sqrt(magSq());
	}
}
