package net.hellspawn.archon.tech

import net.fabricmc.api.ModInitializer
import net.hellspawn.archon.tech.power.ModBlockEntities
import net.hellspawn.archon.tech.power.ModBlocks
import net.hellspawn.archon.tech.util.log.Log
import net.hellspawn.archon.tech.util.log.LogLevel
import kotlin.reflect.jvm.jvmName

object Archontech : ModInitializer {
	const val MOD_ID = "archon-tech"
	private val TAG = Archontech::class.jvmName
	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Log.log(LogLevel.INFO, TAG,"Archon-tech initialized!")
		ModBlocks.registerAll()
		ModBlockEntities.registerAll()
	}
}