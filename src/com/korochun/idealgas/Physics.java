package com.korochun.idealgas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

class Physics implements Runnable {
	
	@SuppressWarnings("serial")
	class Display extends JPanel implements Runnable {
		
		private BufferedImage buffer = new BufferedImage(SIZE.width, SIZE.height, BufferedImage.TYPE_INT_RGB);
		private final Dimension DIM = new Dimension(SIZE.width + 2 * BORDER, SIZE.height + 2 * BORDER);
		
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(buffer, 0, 0, this);
		}

		@Override
		public Dimension getMaximumSize() {
			return DIM;
		}
		@Override
		public Dimension getMinimumSize() {
			return DIM;
		}
		@Override
		public Dimension getPreferredSize() {
			return DIM;
		}
		@Override
		public void run() {
			while (play) {
				long time = Instant.now().toEpochMilli();
				BufferedImage new_buffer = new BufferedImage(DIM.width, DIM.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = new_buffer.createGraphics();
			    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, DIM.width, DIM.height);
				g.setColor(Color.BLACK);
				g.fillRect(BORDER, BORDER, (int) (SIZE.width - pistonX), SIZE.height);
				double multi = Main.getVectorMulti();
				for (Particle part : parts) {
					double size = part.getSize();
					double x = part.x.getX(), y = part.x.getY(), posX = x - size + BORDER, posY = y - size + BORDER;
					g.setColor(part.getColor());
					g.fill(new Ellipse2D.Double(posX, posY, size * 2, size * 2));
					if (Main.isShowVectors()) {
						g.setColor(Color.RED);
						g.draw(new Line2D.Double(x + BORDER, y + BORDER, x + part.v.getX() * multi + BORDER, y + part.v.getY() * multi + BORDER));
					}
				}
				buffer = new_buffer;
				this.repaint();
				try {
					Thread.sleep(Instant.now().toEpochMilli() - time + 16);
				} catch (InterruptedException e) { }
			}
		}
	}
	
	private static final Dimension SIZE = new Dimension(320, 460);
	private static final int BORDER = 10;
	
	private ArrayList<Particle> parts = new ArrayList<Particle>(Graph.LEN * Graph.MAG);
	private Thread phys, upd;
	private boolean play = false;
	private ArrayList<Integer> speedDistribution = new ArrayList<Integer>();
	
	private double pressure = 0, temperature = 0;
	
	static Physics instance = new Physics();
	static Display display = instance.new Display();
	
	private double pistonX = 0;
	
	private Physics() { }

	double getPressure() {
		double p = pressure;
		pressure = 0;
		return p;
	}
	static Dimension getSize() {
		return SIZE;
	}
	double getTemperature() {
		return temperature;
	}

	boolean isPlay() {
		return play;
	}
	void start() {
		for (int i = 0; i < Graph.LEN; i++) {
			speedDistribution.add(0);
		}
		for (int i = 0; i < Main.getParticleCount(); i++) {
			parts.add(new Particle(new Vector(Math.random() * SIZE.width, Math.random() * SIZE.height), new Vector(Math.random() * Main.getParticleSpeed(), Math.random() * Math.PI * 2, true), Main.getParticleMass()));
		}
		for (int i = 0; i < Main.getBigParticleCount(); i++) {
			parts.add(new Particle(new Vector(Math.random() * SIZE.width, Math.random() * SIZE.height), new Vector(Math.random() * Main.getParticleSpeed(), Math.random() * Math.PI * 2, true), 36, Color.RED));
		}
		phys = new Thread(this, "Thread-Physics");
		upd = new Thread(display, "Thread-Display");
		play = true;
		phys.start();
		upd.start();
	}
	void stop() {
		play = false;
		try {
			phys.join();
			upd.join();
		} catch (InterruptedException e) { }
		pistonX = 0;
		parts = new ArrayList<Particle>(Graph.LEN * Graph.MAG);
		speedDistribution = new ArrayList<Integer>();
		pressure = 0;
		temperature = 0;
	}
	
	@Override
	public void run() {
		while (play) {
			long time = Instant.now().toEpochMilli();
			if (Main.isRun()) {
				ArrayList<Particle> new_parts = new ArrayList<Particle>();
				ArrayList<Double> speeds = new ArrayList<Double>();
				int[] new_speedDistribution = new int[Graph.LEN * Graph.MAG];
				for (int i = 0; i < new_speedDistribution.length; i++) {
					new_speedDistribution[i] = 0;
				}
				double new_temperature = 0;
				for (Particle part : parts) {
					new_parts.add(part.update());
					new_temperature += part.getMass() * part.v.magSq() / 2;
					pressure += part.getPressure();
					speeds.add(part.v.mag());
				}
				if (!speeds.isEmpty()) {
					double minSpeed = Collections.min(speeds);
					for (double speed : speeds) {
						new_speedDistribution[(int) ((speed - minSpeed) * (Graph.LEN * Graph.MAG - 1) / (Collections.max(speeds) - minSpeed))] += 1;
					}
				}
				if (Main.isMovePiston()) {
					double pistonSpeed = Main.getPistonSpeed(), pistonLimit = Main.getPistonLimit();
					if (Math.abs(pistonX - pistonLimit) < pistonSpeed) {
						Main.setPistonMove(false);
					} else if (pistonX < pistonLimit) {
						pistonX += pistonSpeed;
					} else if (pistonX > pistonLimit) {
						pistonX -= pistonSpeed;
					}
				}
				for (int i = 0; i < parts.size(); i++) {
					Particle p = new_parts.get(i);
					double cellX = p.x.getX() / 10, cellY = p.x.getY() / 10;
					for (int j = 0; j < parts.size(); j++) {
						Particle q = new_parts.get(j);
						if (Math.abs(cellX - q.x.getX() / 10) < 2 && Math.abs(cellY - p.x.getY() / 10) < 2) {
							p.collide(q);
						}
					}
				}
				speedDistribution = new ArrayList<Integer>();
				for (int i = 0; i < Graph.LEN; i++) {
					speedDistribution.add(new_speedDistribution[i]);
				}
				temperature = new_temperature;
				parts = new_parts;
			}
			try {
				Thread.sleep(Instant.now().toEpochMilli() - time + 16);
			} catch (InterruptedException e) { }
		}
	}

	ArrayList<Integer> getSpeedDistribution() {
		return speedDistribution;
	}

	double getPistonX() {
		return pistonX;
	}

	double getVolume() {
		return (SIZE.getWidth() - pistonX) * SIZE.getHeight();
	}
}
