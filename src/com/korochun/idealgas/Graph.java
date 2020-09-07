package com.korochun.idealgas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class Graph extends JPanel implements Runnable {
	
	private static final Dimension SIZE = new Dimension(300, 110);
	static final int LEN = 47;
	static final int MAG = 1;
	
	private ArrayDeque<Double> pressure = new ArrayDeque<Double>(LEN), temperature = new ArrayDeque<Double>(LEN), volume = new ArrayDeque<Double>(LEN);
	private BufferedImage buffer = new BufferedImage(SIZE.width, SIZE.height, BufferedImage.TYPE_INT_RGB);
	private boolean play = false;
	private Thread thread;
	
	static Graph instance = new Graph();
	
	private Graph() { }
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}
	
	void start() {
		for (int i = 0; i < LEN; i++) {
			pressure.add(0d);
			temperature.add(0d);
			volume.add(0d);
		}
		thread = new Thread(this, "Thread-Graph");
		play = true;
		thread.start();
	}
	void stop() {
		play = false;
		try {
			thread.join();
		} catch (InterruptedException e) { }
		
		pressure = new ArrayDeque<Double>(LEN);
		temperature = new ArrayDeque<Double>(LEN);
		volume = new ArrayDeque<Double>(LEN);
	}
	
	@Override
	public void run() {
		while (play) {
			long time = Instant.now().toEpochMilli();
			if (Main.isRun()) {
				pressure.remove();
				temperature.remove();
				volume.remove();
				pressure.add(Physics.instance.getPressure());
				temperature.add(Physics.instance.getTemperature());
				volume.add(Physics.instance.getVolume());
				BufferedImage new_buffer = new BufferedImage(SIZE.width, SIZE.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = new_buffer.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, SIZE.width, SIZE.height);
				g.setColor(Color.BLACK);
				g.fillRect(5, 5, 140, 45);
				g.fillRect(155, 5, 140, 45);
				g.fillRect(5, 60, 140, 45);
				g.fillRect(155, 60, 140, 45);
				g.setColor(Color.GRAY);
				ArrayList<Integer> speedDistribution = Physics.instance.getSpeedDistribution();
				double pMax = Collections.max(pressure), sMax = Collections.max(speedDistribution.subList(0, LEN)), tMax = Collections.max(temperature), vMax = Collections.max(volume);
				double pLim = pMax / 200, sLim = sMax / 15, tLim = tMax / 3000, vLim = vMax / 30000;
				for (int dy = 1; dy <= pLim; dy++) {
					g.draw(new Line2D.Double(5, 50 - (45 / pLim) * dy, 144, 50 - (45 / pLim) * dy));
				}
				for (int dy = 1; dy <= sLim; dy++) {
					g.draw(new Line2D.Double(155, 50 - (45 / sLim) * dy, 294, 50 - (45 / sLim) * dy));
				}
				for (int dy = 1; dy <= tLim; dy++) {
					g.draw(new Line2D.Double(5, 105 - (45 / tLim) * dy, 144, 105 - (45 / tLim) * dy));
				}
				for (int dy = 1; dy <= vLim; dy++) {
					g.draw(new Line2D.Double(155, 105 - (45 / vLim) * dy, 294, 105 - (45 / vLim) * dy));
				}
				Double[] ps = pressure.toArray(new Double[LEN]), ts = temperature.toArray(new Double[LEN]), vs = volume.toArray(new Double[LEN]);
				Path2D.Double press = new Path2D.Double(), speed = new Path2D.Double(), temp = new Path2D.Double(), vol = new Path2D.Double();
				if (pMax == 0) {
					press.moveTo(0, 45);
				} else {
					press.moveTo(0, 45 * (1 - ps[0] / pMax));
				}
				if (sMax == 0) {
					speed.moveTo(0, 45);
				} else {
					speed.moveTo(0, 45 * (1 - speedDistribution.get(0) / sMax));
				}
				if (tMax == 0) {
					temp.moveTo(0, 45);
				} else {
					temp.moveTo(0, 45 * (1 - ts[0] / tMax));
				}
				if (vMax == 0) {
					vol.moveTo(0, 45);
				} else {
					vol.moveTo(0, 45 * (1 - vs[0] / vMax));
				}
				for (int i = 1; i < LEN; i++) {
					if (pMax == 0) {
						press.lineTo(3 * i, 45);
					} else {
						press.lineTo(3 * i, 45 * (1 - ps[i] / pMax));
					}
					if (sMax == 0) {
						speed.lineTo(3 * i, 45);
					} else {
						speed.lineTo(3 * i, 45 * (1 - speedDistribution.get(i) / sMax));
					}
					if (tMax == 0) {
						temp.lineTo(3 * i, 45);
					} else {
						temp.lineTo(3 * i, 45 * (1 - ts[i] / tMax));
					}
					if (vMax == 0) {
						vol.lineTo(3 * i, 45);
					} else {
						vol.lineTo(3 * i, 45 * (1 - vs[i] / vMax));
					}
				}
				press.transform(AffineTransform.getTranslateInstance(5, 5));
				speed.transform(AffineTransform.getTranslateInstance(155, 5));
				temp.transform(AffineTransform.getTranslateInstance(5, 60));
				vol.transform(AffineTransform.getTranslateInstance(155, 60));
				g.setColor(Color.BLUE);
				g.draw(press);
				g.setColor(Color.MAGENTA);
				g.draw(speed);
				g.setColor(Color.RED);
				g.draw(temp);
				g.setColor(Color.GREEN);
				g.draw(vol);
				buffer = new_buffer;
				this.repaint();
			}
			try {
				Thread.sleep(Instant.now().toEpochMilli() - time + 1000);
			} catch (InterruptedException e) { }
		}
	}
	@Override
	public Dimension getMaximumSize() {
		return SIZE;
	}
	@Override
	public Dimension getMinimumSize() {
		return SIZE;
	}
	@Override
	public Dimension getPreferredSize() {
		return SIZE;
	}
}
