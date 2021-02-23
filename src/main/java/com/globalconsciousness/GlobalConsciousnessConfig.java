package com.globalconsciousness;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(GlobalConsciousnessPlugin.CONFIG_GROUP_KEY)
public interface GlobalConsciousnessConfig extends Config {

	@ConfigItem(
			keyName = "itemName",
			name = "Item Name",
			description = "Name of item to display.",
			position = 1,
			hidden = true
	)
	default String itemName()
	{
		return "";
	}

	@ConfigItem(
			keyName = "itemId",
			name = "Item ID",
			description = "ID of item to display.",
			position = 2,
			hidden = true
	)
	default int itemId()
	{
		return 20594;
	}

	@ConfigItem(
			keyName = "iconSpeed",
			name = "Speed",
			description = "Speed to travel across the screen.",
			position = 3,
			hidden = true
	)
	@Range(
			min = 1,
			max = 20
	)
	default int iconSpeed() {
		return 1;
	}

	@ConfigItem(
			keyName = "iconScale",
			name = "Scale",
			description = "Size of floating icon.",
			position = 4,
			hidden = true
	)
	@Range(
			min = 1,
			max = 5
	)
	default int iconScale() {
		return 1;
	}

	@ConfigItem(
			keyName = "iconOpacity",
			name = "Opacity",
			description = "Opacity of floating icon.",
			position = 5,
			hidden = true
	)
	@Range(
			min = 0,
			max = 100
	)
	default int iconOpacity() {
		return 100;
	}

}
