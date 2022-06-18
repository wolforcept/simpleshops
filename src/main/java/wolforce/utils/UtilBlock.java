package wolforce.utils;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class UtilBlock {

	public static Set<BlockState> getAllStates(Block blockIn) {
		return ImmutableSet.copyOf(blockIn.getStateDefinition().getPossibleStates());
	}

}
