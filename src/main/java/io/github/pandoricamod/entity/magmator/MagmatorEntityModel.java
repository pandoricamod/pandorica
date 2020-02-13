package io.github.pandoricamod.entity.magmator;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;

public class MagmatorEntityModel extends EntityModel<MagmatorEntity> {
    private final ModelPart bone;

    public MagmatorEntityModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelPart(this);
        bone.setPivot(0.0F, 24.0F, 0.0F);
        bone.addCuboid("leg_l", -14.0F, -37.0F, -4.0F, 8, 37, 8, 0.0F, 0, 53);
        bone.addCuboid("leg_r", 8.0F, -37.0F, -4.0F, 8, 37, 8, 0.0F, 0, 53);
        bone.addCuboid("crest", -8.0F, -28.0F, -8.0F, 18, 11, 16, 0.0F, 0, 0);
        bone.addCuboid("head", -7.0F, -37.0F, -7.0F, 16, 12, 14, 0.0F, 0, 27);
    }

    @Override
    public void setAngles(MagmatorEntity magmatorEntity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {}

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
