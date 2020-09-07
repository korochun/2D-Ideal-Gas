package com.korochun.idealgas;

import java.awt.Color;

public class Particle {
	
	Vector x, v;
	
	private Color c;
	private double mass, pressure, size;
	
	Particle(Vector x, Vector v, double mass) {
		this(x, v, mass, Color.BLUE);
	}
	
	Particle(Vector x, Vector v, double mass, Color c) {
		this.x = x;
		this.v = v;
		this.mass = mass;
		this.size = Math.sqrt(mass);
		this.c = c;
	}
	/* ----- LEGACY CODE ----- */
	void collide(Particle other) {
		Vector displ = x.sub(other.x);
		double distSq = displ.magSq();
		if (distSq < Math.pow(size + other.size, 2) && x.add(v).sub(other.x.add(other.v)).magSq() < distSq) {
			double masses = mass + other.mass;
			Vector op = displ.mult(2 * v.sub(other.v).mult(displ) / distSq / masses);
			v = v.sub(op.mult(other.mass));
			other.v = other.v.add(op.mult(mass));
		}
	}
	/* ----- LEGACY END  ----- */
	Color getColor() {
		return c;
	}
	double getMass() {
		return mass;
	}
	double getPressure() {
		return pressure;
	}
	double getSize() {
		return size;
	}
	Particle update() {
		Vector x = this.x.add(v);
		Vector v = this.v.clone();
		
		double posX = x.getX(), posY = x.getY(), vecX = v.getX(), vecY = v.getY();
		if (posX < size) {
			if (vecX < 0) {
				v = v.setX(-vecX);
			} else if (vecX == 0) {
				v = v.setX(size - posX);
			}
			pressure += mass * v.magSq() / Math.abs(v.getX()) / 4;
		} else if (posX > Physics.getSize().width - Physics.instance.getPistonX() - size) {
			double add = 0;
			if (Main.isMovePiston()) {
				add = Main.getPistonSpeed();
			}
			if (vecX > 0) {
				v = v.setX(-vecX - add);
			} else if (Math.abs(vecX) < add) {
				v = v.setX(Physics.getSize().width - Physics.instance.getPistonX() - size - posX - add);
			}
			pressure += mass * v.magSq() / Math.abs(v.getX()) / 4;
		}
		if (posY < size) {
			if (vecY < 0) {
				v = v.setY(-vecY);
			} else if (vecY == 0) {
				v = v.setY(size - posY);
			}
			pressure += mass * v.magSq() / Math.abs(v.getY()) / 4;
		} else if (posY > Physics.getSize().height - size) {
			if (vecY > 0) {
				v = v.setY(-vecY);
			} else if (vecY == 0) {
				v = v.setY(Physics.getSize().height - size - posY);
			}
			pressure += mass * v.magSq() / Math.abs(v.getY()) / 4;
		}
		return new Particle(x, v, mass, c);
	}
}
