package sts.caster.core.frozenpile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import com.megacrit.cardcrawl.vfx.ExhaustPileParticle;
import sts.caster.patches.frozenpile.FrozenPileEnums;

@Deprecated
public class FrozenCardsPanel extends AbstractPanel {
    //    private static final TutorialStrings tutorialStrings;
    public static final String MSG = "Click to view cards Frozen this combat.";
    public static final String LABEL = "Frozen Cards";
    public static float fontScale;
    public static final float FONT_POP_SCALE = 2.0f;
    private static final float COUNT_CIRCLE_W;
    public static int totalCount;
    private GlyphLayout gl;
    private Hitbox hb;
    public static float energyVfxTimer;
    public static final float ENERGY_VFX_TIME = 2.0f;

    public FrozenCardsPanel() {
        super(Settings.WIDTH - 70.0f * Settings.scale, 264.0f * Settings.scale, Settings.WIDTH + 100.0f * Settings.scale, 264.0f * Settings.scale, 0.0f, 0.0f, null, false);
        this.gl = new GlyphLayout();
        this.hb = new Hitbox(0.0f, 0.0f, 100.0f * Settings.scale, 100.0f * Settings.scale);
    }

    @Override
    public void updatePositions() {
        super.updatePositions();
        if (!this.isHidden && FrozenPileManager.frozenPile.size() > 0) {
            this.hb.update();
            this.updateVfx();
        }
        if (this.hb.hovered && (!AbstractDungeon.isScreenUp || AbstractDungeon.screen == FrozenPileEnums.FROZEN_VIEW || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT)) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        }
        if ((this.hb.clicked) && AbstractDungeon.screen == FrozenPileEnums.FROZEN_VIEW) {
            this.hb.clicked = false;
            this.hb.hovered = false;
            CardCrawlGame.sound.play("DECK_CLOSE");
            AbstractDungeon.closeCurrentScreen();
            return;
        }
        if ((this.hb.clicked) && AbstractDungeon.overlayMenu.combatPanelsShown && AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.player.isDead) {
            this.hb.clicked = false;
            this.hb.hovered = false;
            if (AbstractDungeon.isScreenUp) {
                if (AbstractDungeon.previousScreen == null) {
                    AbstractDungeon.previousScreen = AbstractDungeon.screen;
                }
            } else {
                AbstractDungeon.previousScreen = null;
            }
            this.openFrozenPile();
        }
    }

    private void openFrozenPile() {
        FrozenPileManager.frozenPileViewScreen.open();
        this.hb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    private void updateVfx() {
        FrozenCardsPanel.energyVfxTimer -= Gdx.graphics.getDeltaTime();
        if (FrozenCardsPanel.energyVfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new ExhaustPileParticle(this.current_x, this.current_y));
            FrozenCardsPanel.energyVfxTimer = 0.05f;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        if (!Settings.hideLowerElements && !FrozenPileManager.frozenPile.isEmpty()) {
            this.hb.move(this.current_x, this.current_y);
            final String msg = Integer.toString(FrozenPileManager.frozenPile.size());
            this.gl.setText(FontHelper.turnNumFont, msg);
            sb.setColor(Color.CYAN.cpy());
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x - COUNT_CIRCLE_W / 2.0f, this.current_y - COUNT_CIRCLE_W / 2.0f, COUNT_CIRCLE_W, COUNT_CIRCLE_W);
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, msg, this.current_x, this.current_y + 2.0f * Settings.scale, Color.LIGHT_GRAY.cpy());
            this.hb.render(sb);
            if (this.hb.hovered && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
                TipHelper.renderGenericTip(1550.0f * Settings.scale, 450.0f * Settings.scale, LABEL, MSG);
            }
        }
    }

    static {
        FrozenCardsPanel.fontScale = 1.0f;
        COUNT_CIRCLE_W = 128.0f * Settings.scale;
        FrozenCardsPanel.totalCount = 0;
        FrozenCardsPanel.energyVfxTimer = 0.0f;
    }
}