/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2012 University of Dundee & Open Microscopy Environment.
 *  All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.util;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.openmicroscopy.shoola.agents.util.browser.DataNode;
import org.openmicroscopy.shoola.util.ui.IconManager;


/** 
 * Provides a wrapped renderer for displaying tooltip information on mouse hover
 *
 * @author Scott Littlewood, <a href="mailto:sylittlewood@dundee.ac.uk">sylittlewood@dundee.ac.uk</a>
 * @since Beta4.4
 */
public class ComboBoxToolTipRenderer extends DefaultListCellRenderer {
	
	/** 
	 * The icon indicating that the data object is not owned by the
	 * specified user.
	 */
	private static final Icon NOT_OWNED_ICON;
	
	static {
		IconManager icons = IconManager.getInstance();
		NOT_OWNED_ICON = icons.getIcon(IconManager.NOT_OWNED_8);
	}
	
	/** The tool tips to set.*/
	private List<String> tooltips;

	/** Used to check if the user is the owner of the data.*/
	private long userID;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param userID The id of the user.
	 */
	public ComboBoxToolTipRenderer(long userID)
	{
		this.userID = userID;
	}
	
	/**
	 * Sets the tool tip.
	 * @see DefaultListCellRenderer#getListCellRendererComponent(JList,
	 * Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JComponent comp = (JComponent) super.getListCellRendererComponent(list,
				value, index, isSelected, cellHasFocus);

		if (index > -1 && value != null && tooltips != null
				&& tooltips.size() > index) {
			list.setToolTipText(tooltips.get(index));
		}
		comp.setEnabled(true);
		if (value instanceof DataNode) {
			DataNode node = (DataNode) value;
			if (!node.isDefaultNode()) {
				comp.setEnabled(node.getDataObject().canLink());
			}
			if (userID >= 0 && node.getOwner() != null) {
				if (node.getOwner().getId() != userID 
						&& comp instanceof JLabel) {
					((JLabel) comp).setIcon(NOT_OWNED_ICON);
				}
			}
			
		}
		return comp;
	}

	/**
	 * Populates the renderer with the tooltips provided
	 * 
	 * @param tooltips The value to set.
	 */
	public void setTooltips(List<String> tooltips) {
		this.tooltips = tooltips;
	}
}
