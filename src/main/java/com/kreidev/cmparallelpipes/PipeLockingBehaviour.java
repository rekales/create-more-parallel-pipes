package com.kreidev.cmparallelpipes;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.nbt.CompoundTag;

// Maybe I should've just done this from the start
public class PipeLockingBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<PipeLockingBehaviour> TYPE = new BehaviourType<>();

    private boolean locked;

    public PipeLockingBehaviour(SmartBlockEntity be) {
        super(be);
        this.locked = false;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.putBoolean("Locked", locked);
        super.write(nbt, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        locked = nbt.getBoolean("Locked");
        super.read(nbt, clientPacket);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
}