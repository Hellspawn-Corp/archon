package net.hellspawn.archon.tech.power.blocks

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.block.BlockState

class GeneratorBlockEntity(
    type: BlockEntityType<GeneratorBlockEntity>,
    pos: BlockPos,
    state: BlockState
) : BlockEntity(type, pos, state) {
    private var energy: Int = 0

    fun tick() {
        generateEnergy()
    }

    fun generateEnergy() {
        energy += 10
    }

    fun getEnergy(): Int = energy
}