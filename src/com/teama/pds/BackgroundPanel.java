package com.teama.pds;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel for implementing
 */
class BackgroundPanel extends JPanel {
	Image image;

	public BackgroundPanel(String image_path) {
		try {
			image = (new ImageIcon(image_path)).getImage();
		} catch (Exception e) {
			//failed to load background image. oh well :C
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(
					image,
					(this.getWidth() / 2) - (image.getWidth(this) / 2),
					(this.getHeight() / 2) - (image.getHeight(this) / 2),
					image.getWidth(this),
					image.getHeight(this),
					this);
		}
	}
}