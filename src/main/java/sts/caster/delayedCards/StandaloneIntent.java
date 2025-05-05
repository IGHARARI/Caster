package sts.caster.delayedCards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashIntentEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;

import java.util.*;

public abstract class StandaloneIntent {
//
//    public static final String[] TEXT;
//
//    protected static final float TIP_X_THRESHOLD;
//    protected static final float MULTI_TIP_Y_OFFSET;
//    protected static final float TIP_OFFSET_R_X;
//    protected static final float TIP_OFFSET_L_X;
//    protected static final float TIP_OFFSET_Y;
//
//
//    private static final int INTENT_W = 128;
//    private static final float INTENT_HB_W;
//
//    public static Comparator<com.megacrit.cardcrawl.monsters.AbstractMonster> sortByHitbox;
//
//    static {
//        INTENT_HB_W = 64.0F * Settings.scale;
//        sortByHitbox = (o1, o2) -> (int) (o1.hb.cX - o2.hb.cX);
//    }
//
//
//    public Hitbox intentHb;
//    public com.megacrit.cardcrawl.monsters.AbstractMonster.Intent intent;
//    public com.megacrit.cardcrawl.monsters.AbstractMonster.Intent tipIntent;
//    public float intentAlpha;
//    public float intentAlphaTarget;
//    public float intentOffsetX;
//
//    public float drawX;
//
//    public Hitbox hb;
//    public float hb_x;
//    public float hb_y;
//    public float hb_w;
//    public float hb_h;
//    public float hbAlpha = 0.0F;
//    public TintEffect tint;
//    public float animX;
//    public float animY;
//    public AnimationState state;
//    public float reticleAlpha;
//    public boolean reticleRendered;
//    protected Texture img;
//    protected float vX;
//    protected float vY;
//    private PowerTip intentTip;
//    private float intentParticleTimer;
//    private float intentAngle;
//    private ArrayList<AbstractGameEffect> intentVfx;
//    private BobEffect bobEffect;
//    private Texture intentImg;
//    private Texture intentBg;
//    private int intentDmg;
//    private int intentBaseDmg;
//    private int intentMultiAmt;
//    private boolean isMultiDmg;
//    private Color intentColor;
//
//
//
//    public StandaloneIntent(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
//
//        this.intentTip = new PowerTip();
//
//        this.intentParticleTimer = 0.0F;
//        this.intentAngle = 0.0F;
//
//        this.intentVfx = new ArrayList();
//
//        this.bobEffect = new BobEffect();
//        this.intent = com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.DEBUG;
//        this.tipIntent = com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.DEBUG;
//        this.intentAlpha = 0.0F;
//        this.intentAlphaTarget = 0.0F;
//        this.intentOffsetX = 0.0F;
//        this.intentImg = null;
//        this.intentBg = null;
//        this.intentDmg = -1;
//        this.intentBaseDmg = -1;
//        this.intentMultiAmt = 0;
//        this.isMultiDmg = false;
//        this.intentColor = Color.WHITE.cpy();
//
//
//
//        this.drawX = (float) Settings.WIDTH * 0.75F + offsetX * Settings.xScale;
//        this.drawY = AbstractDungeon.floorY + offsetY * Settings.yScale;
//        this.hb_w = hb_w * Settings.scale;
//        this.hb_h = hb_h * Settings.xScale;
//        this.hb_x = hb_x * Settings.scale;
//        this.hb_y = hb_y * Settings.scale;
//        if (imgUrl != null) {
//            this.img = ImageMaster.loadImage(imgUrl);
//        }
//
//        this.intentHb = new Hitbox(INTENT_HB_W, INTENT_HB_W);
//        this.hb = new Hitbox(this.hb_w, this.hb_h);
//
//        this.refreshIntentHbLocation();
//    }
//
//    public void refreshIntentHbLocation() {
//        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + INTENT_HB_W / 2.0F);
//    }
//
//    public void update() {
//        this.updateIntent();
//        this.tint.update();
//    }
//
//    public void unhover() {
//        this.hb.hovered = false;
//        this.intentHb.hovered = false;
//    }
//
//    private void updateIntent() {
//        this.bobEffect.update();
//        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
//            this.intentAlpha += Gdx.graphics.getDeltaTime();
//            if (this.intentAlpha > this.intentAlphaTarget) {
//                this.intentAlpha = this.intentAlphaTarget;
//            }
//        } else if (this.intentAlphaTarget == 0.0F) {
//            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 1.5F;
//            if (this.intentAlpha < 0.0F) {
//                this.intentAlpha = 0.0F;
//            }
//        }
//
//        if (!this.isDying && !this.isEscaping) {
//            this.updateIntentVFX();
//        }
//
//        Iterator i = this.intentVfx.iterator();
//
//        while (i.hasNext()) {
//            AbstractGameEffect e = (AbstractGameEffect) i.next();
//            e.update();
//            if (e.isDone) {
//                i.remove();
//            }
//        }
//
//    }
//
//    private void updateIntentVFX() {
//        if (this.intentAlpha > 0.0F) {
//            if (this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.ATTACK_DEBUFF && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.DEBUFF && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.STRONG_DEBUFF && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.DEFEND_DEBUFF) {
//                if (this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.ATTACK_BUFF && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.BUFF && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.DEFEND_BUFF) {
//                    if (this.intent == com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.ATTACK_DEFEND) {
//                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
//                        if (this.intentParticleTimer < 0.0F) {
//                            this.intentParticleTimer = 0.5F;
//                            this.intentVfx.add(new ShieldParticleEffect(this.intentHb.cX, this.intentHb.cY));
//                        }
//                    } else if (this.intent == com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.UNKNOWN) {
//                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
//                        if (this.intentParticleTimer < 0.0F) {
//                            this.intentParticleTimer = 0.5F;
//                            this.intentVfx.add(new UnknownParticleEffect(this.intentHb.cX, this.intentHb.cY));
//                        }
//                    } else if (this.intent == com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.STUN) {
//                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
//                        if (this.intentParticleTimer < 0.0F) {
//                            this.intentParticleTimer = 0.67F;
//                            this.intentVfx.add(new StunStarEffect(this.intentHb.cX, this.intentHb.cY));
//                        }
//                    }
//                } else {
//                    this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
//                    if (this.intentParticleTimer < 0.0F) {
//                        this.intentParticleTimer = 0.1F;
//                        this.intentVfx.add(new BuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
//                    }
//                }
//            } else {
//                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
//                if (this.intentParticleTimer < 0.0F) {
//                    this.intentParticleTimer = 1.0F;
//                    this.intentVfx.add(new DebuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
//                }
//            }
//        }
//
//    }
//
//    public void renderTip(SpriteBatch sb) {
//        this.tips.clear();
//        if (this.intentAlphaTarget == 1.0F && !AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.NONE) {
//            this.tips.add(this.intentTip);
//        }
//
//        Iterator var2 = this.powers.iterator();
//
//        while (var2.hasNext()) {
//            AbstractPower p = (AbstractPower) var2.next();
//            if (p.region48 != null) {
//                this.tips.add(new PowerTip(p.name, p.description, p.region48));
//            } else {
//                this.tips.add(new PowerTip(p.name, p.description, p.img));
//            }
//        }
//
//        if (!this.tips.isEmpty()) {
//            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
//                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
//            } else {
//                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
//            }
//        }
//
//    }
//
//    private void updateIntentTip() {
//        switch (this.intent) {
//            case ATTACK:
//                this.intentTip.header = TEXT[0];
//                if (this.isMultiDmg) {
//                    this.intentTip.body = TEXT[1] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
//                } else {
//                    this.intentTip.body = TEXT[4] + this.intentDmg + TEXT[5];
//                }
//
//                this.intentTip.img = this.getAttackIntentTip();
//                break;
//            case ATTACK_BUFF:
//                this.intentTip.header = TEXT[6];
//                if (this.isMultiDmg) {
//                    this.intentTip.body = TEXT[7] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[8];
//                } else {
//                    this.intentTip.body = TEXT[9] + this.intentDmg + TEXT[5];
//                }
//
//                this.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
//                break;
//            case DEFEND:
//                this.intentTip.header = TEXT[13];
//                this.intentTip.body = TEXT[22];
//                this.intentTip.img = ImageMaster.INTENT_DEFEND;
//                break;
//            default:
//                this.intentTip.header = "NOT SET";
//                this.intentTip.body = "NOT SET";
//                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
//        }
//
//    }
//
//    public void flashIntent() {
//        if (this.intentImg != null) {
//            AbstractDungeon.effectList.add(new FlashIntentEffect(this.intentImg, this));
//        }
//
//        this.intentAlphaTarget = 0.0F;
//    }
//
//    public void createIntent() {
//        this.intent = this.move.intent;
//        this.intentParticleTimer = 0.5F;
//        this.nextMove = this.move.nextMove;
//        this.intentBaseDmg = this.move.baseDamage;
//        if (this.move.baseDamage > -1) {
//            this.calculateDamage(this.intentBaseDmg);
//            if (this.move.isMultiDamage) {
//                this.intentMultiAmt = this.move.multiplier;
//                this.isMultiDmg = true;
//            } else {
//                this.intentMultiAmt = -1;
//                this.isMultiDmg = false;
//            }
//        }
//
//        this.intentImg = this.getIntentImg();
//        this.tipIntent = this.intent;
//        this.intentAlpha = 0.0F;
//        this.intentAlphaTarget = 1.0F;
//        this.updateIntentTip();
//    }
//
//    private Texture getIntentImg() {
//        switch (this.intent) {
//            case ATTACK:
//                return this.getAttackIntent();
//            case ATTACK_BUFF:
//                return this.getAttackIntent();
//            case DEFEND:
//                return ImageMaster.INTENT_DEFEND_L;
//            case DEFEND_BUFF:
//                return ImageMaster.INTENT_DEFEND_BUFF_L;
//            case MAGIC:
//                return ImageMaster.INTENT_MAGIC_L;
//            case SLEEP:
//                return ImageMaster.INTENT_SLEEP_L;
//            case STUN:
//                return null;
//            case UNKNOWN:
//                return ImageMaster.INTENT_UNKNOWN_L;
//            default:
//                return ImageMaster.INTENT_UNKNOWN_L;
//        }
//    }
//
//    protected Texture getAttackIntent(int dmg) {
//        if (dmg < 5) {
//            return ImageMaster.INTENT_ATK_1;
//        } else if (dmg < 10) {
//            return ImageMaster.INTENT_ATK_2;
//        } else if (dmg < 15) {
//            return ImageMaster.INTENT_ATK_3;
//        } else if (dmg < 20) {
//            return ImageMaster.INTENT_ATK_4;
//        } else if (dmg < 25) {
//            return ImageMaster.INTENT_ATK_5;
//        } else {
//            return dmg < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
//        }
//    }
//
//    protected Texture getAttackIntent() {
//        int tmp;
//        if (this.isMultiDmg) {
//            tmp = this.intentDmg * this.intentMultiAmt;
//        } else {
//            tmp = this.intentDmg;
//        }
//
//        if (tmp < 5) {
//            return ImageMaster.INTENT_ATK_1;
//        } else if (tmp < 10) {
//            return ImageMaster.INTENT_ATK_2;
//        } else if (tmp < 15) {
//            return ImageMaster.INTENT_ATK_3;
//        } else if (tmp < 20) {
//            return ImageMaster.INTENT_ATK_4;
//        } else if (tmp < 25) {
//            return ImageMaster.INTENT_ATK_5;
//        } else {
//            return tmp < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
//        }
//    }
//
//    private Texture getAttackIntentTip() {
//        int tmp;
//        if (this.isMultiDmg) {
//            tmp = this.intentDmg * this.intentMultiAmt;
//        } else {
//            tmp = this.intentDmg;
//        }
//
//        if (tmp < 5) {
//            return ImageMaster.INTENT_ATK_TIP_1;
//        } else if (tmp < 10) {
//            return ImageMaster.INTENT_ATK_TIP_2;
//        } else if (tmp < 15) {
//            return ImageMaster.INTENT_ATK_TIP_3;
//        } else if (tmp < 20) {
//            return ImageMaster.INTENT_ATK_TIP_4;
//        } else if (tmp < 25) {
//            return ImageMaster.INTENT_ATK_TIP_5;
//        } else {
//            return tmp < 30 ? ImageMaster.INTENT_ATK_TIP_6 : ImageMaster.INTENT_ATK_TIP_7;
//        }
//    }
//
//    public void render(SpriteBatch sb) {
//        if (!this.isDead && !this.escaped) {
//
//            if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
//                this.renderIntentVfxBehind(sb);
//                this.renderIntent(sb);
//                this.renderIntentVfxAfter(sb);
//                this.renderDamageRange(sb);
//            }
//            this.intentHb.render(sb);
//        }
//
//    }
//
//    private void renderDamageRange(SpriteBatch sb) {
//        if (this.intent.name().contains("ATTACK")) {
//            if (this.isMultiDmg) {
//                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, this.intentDmg + "x" + this.intentMultiAmt, this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
//            } else {
//                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentDmg), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
//            }
//        }
//
//    }
//
//    private void renderIntentVfxBehind(SpriteBatch sb) {
//        Iterator var2 = this.intentVfx.iterator();
//
//        while (var2.hasNext()) {
//            AbstractGameEffect e = (AbstractGameEffect) var2.next();
//            if (e.renderBehind) {
//                e.render(sb);
//            }
//        }
//
//    }
//
//    private void renderIntentVfxAfter(SpriteBatch sb) {
//        Iterator var2 = this.intentVfx.iterator();
//
//        while (var2.hasNext()) {
//            AbstractGameEffect e = (AbstractGameEffect) var2.next();
//            if (!e.renderBehind) {
//                e.render(sb);
//            }
//        }
//
//    }
//
//    private void renderIntent(SpriteBatch sb) {
//        this.intentColor.a = this.intentAlpha;
//        sb.setColor(this.intentColor);
//        if (this.intentBg != null) {
//            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.intentAlpha / 2.0F));
//                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
//        }
//
//        if (this.intentImg != null && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.UNKNOWN && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.STUN) {
//            if (this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.DEBUFF && this.intent != com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.STRONG_DEBUFF) {
//                this.intentAngle = 0.0F;
//            } else {
//                this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
//            }
//
//            sb.setColor(this.intentColor);
//                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, this.intentAngle, 0, 0, 128, 128, false, false);
//
//        }
//
//    }
//
//    protected void updateHitbox(float hb_x, float hb_y, float hb_w, float hb_h) {
//        this.hb_w = hb_w * Settings.scale;
//        this.hb_h = hb_h * Settings.xScale;
//        this.hb_y = hb_y * Settings.scale;
//        this.hb_x = hb_x * Settings.scale;
//        this.hb = new Hitbox(this.hb_w, this.hb_h);
//        this.hb.move(this.drawX + this.hb_x + this.animX, this.drawY + this.hb_y + this.hb_h / 2.0F);
//        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + 32.0F * Settings.scale);
//    }
//
//    public int getIntentDmg() {
//        return this.intentDmg;
//    }
//
//    public int getIntentBaseDmg() {
//        return this.intentBaseDmg;
//    }
//
//    public void setIntentBaseDmg(int amount) {
//        this.intentBaseDmg = amount;
//    }

}
