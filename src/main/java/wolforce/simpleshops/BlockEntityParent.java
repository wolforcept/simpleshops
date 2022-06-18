package wolforce.simpleshops;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityParent extends BlockEntity {
	public BlockEntityParent(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		writePacketNBT(tag);
	}

	@Nonnull
	@Override
	public final CompoundTag getUpdateTag() {
		var tag = new CompoundTag();
		writePacketNBT(tag);
		return tag;
	}

	@Override
	public void load(@Nonnull CompoundTag tag) {
		super.load(tag);
		readPacketNBT(tag);
	}

	public void writePacketNBT(CompoundTag cmp) {
	}

	public void readPacketNBT(CompoundTag cmp) {
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}