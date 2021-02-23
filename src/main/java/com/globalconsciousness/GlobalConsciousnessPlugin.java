package com.globalconsciousness;

import com.google.inject.Provides;

import lombok.extern.slf4j.Slf4j;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.game.chatbox.ChatboxItemSearch;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.game.chatbox.ChatboxTextInput;
import net.runelite.client.game.ItemManager;
import net.runelite.api.GameState;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.ClientUI;

import javax.inject.Inject;
import javax.swing.*;

@Slf4j
@PluginDescriptor(
		name = "Global Consciousness",
		description = "Channel your inner Rendi to unlock the power of global consciousness and... just get the drop lul."
)

public class GlobalConsciousnessPlugin extends Plugin {

	static final String CONFIG_GROUP_KEY = "globalconsciousness";

	public String itemName = "";
	public int itemId;
	public int iconSpeed;
	public int iconScale;
	public int iconOpacity;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private GlobalConsciousnessConfig globalConsciousnessConfig;

	@Inject
	private ChatboxItemSearch itemSearch;

	@Inject
	@Getter
	private ClientThread clientThread;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ConfigManager configManager;

	private NavigationButton navButton;
	private GlobalConsciousnessPanel panel;


	@Provides
	GlobalConsciousnessConfig getConfig(ConfigManager configManager) {
		return configManager.getConfig(GlobalConsciousnessConfig.class);
	};

	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		this.itemName = globalConsciousnessConfig.itemName();
		this.itemId = globalConsciousnessConfig.itemId();
		this.iconSpeed = globalConsciousnessConfig.iconSpeed();
		this.iconScale = globalConsciousnessConfig.iconScale();
		this.iconOpacity = globalConsciousnessConfig.iconOpacity();
		panel.currentItemName.setText(globalConsciousnessConfig.itemName());
	}

	@Inject
	private GlobalConsciousnessOverlay globalConsciousnessOverlay;

	@Override
	public void startUp() {
		overlayManager.add(globalConsciousnessOverlay);
		globalConsciousnessOverlay.x = 0;
		globalConsciousnessOverlay.y = 0;
		this.itemName = globalConsciousnessConfig.itemName();
		this.itemId = globalConsciousnessConfig.itemId();
		this.iconSpeed = globalConsciousnessConfig.iconSpeed();
		this.iconScale = globalConsciousnessConfig.iconScale();
		this.iconOpacity = globalConsciousnessConfig.iconOpacity();

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "brain-icon.png");
		panel = new GlobalConsciousnessPanel(client, globalConsciousnessConfig, this, configManager);
		navButton = NavigationButton.builder()
				.tooltip("Global Consciousness")
				.icon(icon)
				.priority(6)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(globalConsciousnessOverlay);
		clientToolbar.removeNavigation(navButton);
	}

	public void updateFromSearch()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			JOptionPane.showMessageDialog(panel,
					"You must be logged in to search.",
					"Cannot Search for Item",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		itemSearch
			.tooltipText("Set item to")
			.onItemSelected((itemId) -> {
				clientThread.invokeLater(() ->
				{
					int finalId = itemManager.canonicalize(itemId);
					final String itemName = itemManager.getItemComposition(finalId).getName();
					configManager.setConfiguration(CONFIG_GROUP_KEY, "itemName", itemName);
					configManager.setConfiguration(CONFIG_GROUP_KEY, "itemId", finalId);
				});
			})
			.build();
	};
}
