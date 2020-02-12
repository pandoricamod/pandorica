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
        bone.addCuboid("bone", 3.0F, -37.0F, 0.0F, 8, 37, 8, 0.0F, 0, 53);
        bone.addCuboid("bone", -19.0F, -37.0F, 0.0F, 8, 37, 8, 0.0F, 0, 53);

        ModelPart bone2 = new ModelPart(this);
        bone2.setPivot(0.0F, 0.0F, 0.0F);
        bone.addChild(bone2);
        bone2.addCuboid("bone2", -12.0F, -33.0F, -3.0F, 16, 12, 14, 0.0F, 0, 27);
        bone2.addCuboid("bone2", -13.0F, -23.0F, -4.0F, 18, 11, 16, 0.0F, 0, 0);
    }

    @Override
    public void setAngles(MagmatorEntity magmatorEntity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {}

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
