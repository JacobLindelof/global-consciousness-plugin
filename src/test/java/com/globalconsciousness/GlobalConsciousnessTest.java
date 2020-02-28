package com.globalconsciousness;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GlobalConsciousnessTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GlobalConsciousnessPlugin.class);
		RuneLite.main(args);
	}
}