package net.hellspawn.archon.tech.power.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class GeneratorBlockEntity(
    type: BlockEntityType<GeneratorBlockEntity>,
    pos: BlockPos,
    state: BlockState
) : BlockEntity(type, pos, state) {
    companion object {
        const val ENERGY_KEY = "energy"
    }

    private var energy: Int = 0

    fun tick() {
        generateEnergy()
    }

    fun generateEnergy() {
        energy += 10
        setChanged() // Mark the block entity as dirty to ensure data is saved
    }

    fun getEnergy(): Int = energy
    @Override
    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)
        view.putInt(ENERGY_KEY, energy)
    }

    @Override
    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)
        energy = view.getIntOr(ENERGY_KEY, 0)
    }
}