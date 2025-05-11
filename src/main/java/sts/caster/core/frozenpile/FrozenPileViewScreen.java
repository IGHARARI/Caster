package sts.caster.core.frozenpile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import sts.caster.patches.frozenpile.FrozenPileEnums;

import java.util.ArrayList;

@Deprecated
public class FrozenPileViewScreen implements ScrollBarListener {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FrozenViewScreen");
    public static final String[] TEXT = uiStrings.TEXT;
    private CardGroup frozenPileCopy;
    public boolean isHovered;
    private static final int CARDS_PER_LINE = 5;
    private boolean grabbedScreen;
    private float grabStartY;
    private float currentDiffY;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private static final String DESC = TEXT[0];
    private AbstractCard hoveredCard;
    private int prevDeckSize;
    private static final float SCROLL_BAR_THRESHOLD = 500.0f * Settings.scale;
    private ScrollBar scrollBar;
    private AbstractCard controllerCard;

    public FrozenPileViewScreen() {
        this.frozenPileCopy = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        this.isHovered = false;
        this.grabbedScreen = false;
        this.grabStartY = 0.0f;
        this.currentDiffY = 0.0f;
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.hoveredCard = null;
        this.prevDeckSize = 0;
        this.controllerCard = null;
        FrozenPileViewScreen.drawStartX = Settings.WIDTH;
        FrozenPileViewScreen.drawStartX -= 5.0f * AbstractCard.IMG_WIDTH * 0.75f;
        FrozenPileViewScreen.drawStartX -= 4.0f * Settings.CARD_VIEW_PAD_X;
        FrozenPileViewScreen.drawStartX /= 2.0f;
        FrozenPileViewScreen.drawStartX += AbstractCard.IMG_WIDTH * 0.75f / 2.0f;
        FrozenPileViewScreen.padX = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
        FrozenPileViewScreen.padY = AbstractCard.IMG_HEIGHT * 0.75f + Settings.CARD_VIEW_PAD_Y;
        (this.scrollBar = new ScrollBar(this)).move(0.0f, -30.0f * Settings.scale);
    }

    public void update() {
        boolean isDraggingScrollBar = false;
        if (this.shouldShowScrollBar()) {
            isDraggingScrollBar = this.scrollBar.update();
        }
        if (!isDraggingScrollBar) {
            this.updateScrolling();
        }
        if (this.frozenPileCopy.group.size() > 0) {
            this.updateControllerInput();
        }
        if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen && !AbstractDungeon.topPanel.selectPotionMode) {
            if (Gdx.input.getY() > Settings.HEIGHT * 0.7f) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if (Gdx.input.getY() < Settings.HEIGHT * 0.3f) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }
        }
        this.updatePositions();
        if (Settings.isControllerMode && this.controllerCard != null) {
            Gdx.input.setCursorPosition((int) this.controllerCard.hb.cX, (int) (Settings.HEIGHT - this.controllerCard.hb.cY));
        }
    }

    private void updateControllerInput() {
        if (!Settings.isControllerMode || AbstractDungeon.topPanel.selectPotionMode) {
            return;
        }
        boolean anyHovered = false;
        int index = 0;
        for (final AbstractCard c : this.frozenPileCopy.group) {
            if (c.hb.hovered) {
                anyHovered = true;
                break;
            }
            ++index;
        }
        if (!anyHovered) {
            Gdx.input.setCursorPosition((int) this.frozenPileCopy.group.get(0).hb.cX, Settings.HEIGHT - (int) this.frozenPileCopy.group.get(0).hb.cY);
            this.controllerCard = this.frozenPileCopy.group.get(0);
        } else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && this.frozenPileCopy.size() > CARDS_PER_LINE) {
            index -= CARDS_PER_LINE;
            if (index < 0) {
                final int wrap = this.frozenPileCopy.size() / CARDS_PER_LINE;
                index += wrap * CARDS_PER_LINE;
                if (index + CARDS_PER_LINE < this.frozenPileCopy.size()) {
                    index += CARDS_PER_LINE;
                }
            }
            Gdx.input.setCursorPosition((int) this.frozenPileCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.frozenPileCopy.group.get(index).hb.cY);
            this.controllerCard = this.frozenPileCopy.group.get(index);
        } else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && this.frozenPileCopy.size() > CARDS_PER_LINE) {
            if (index < this.frozenPileCopy.size() - CARDS_PER_LINE) {
                index += CARDS_PER_LINE;
            } else {
                index %= CARDS_PER_LINE;
            }
            Gdx.input.setCursorPosition((int) this.frozenPileCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.frozenPileCopy.group.get(index).hb.cY);
            this.controllerCard = this.frozenPileCopy.group.get(index);
        } else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
            if (index % CARDS_PER_LINE > 0) {
                --index;
            } else {
                index += 4;
                if (index > this.frozenPileCopy.size() - 1) {
                    index = this.frozenPileCopy.size() - 1;
                }
            }
            Gdx.input.setCursorPosition((int) this.frozenPileCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.frozenPileCopy.group.get(index).hb.cY);
            this.controllerCard = this.frozenPileCopy.group.get(index);
        } else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
            if (index % CARDS_PER_LINE < 4) {
                if (++index > this.frozenPileCopy.size() - 1) {
                    index -= this.frozenPileCopy.size() % CARDS_PER_LINE;
                }
            } else {
                index -= 4;
                if (index < 0) {
                    index = 0;
                }
            }
            Gdx.input.setCursorPosition((int) this.frozenPileCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.frozenPileCopy.group.get(index).hb.cY);
            this.controllerCard = this.frozenPileCopy.group.get(index);
        }
    }

    private void updateScrolling() {
        final int y = InputHelper.mY;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }
            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartY = y - this.currentDiffY;
            }
        } else if (InputHelper.isMouseDown) {
            this.currentDiffY = y - this.grabStartY;
        } else {
            this.grabbedScreen = false;
        }
        if (this.prevDeckSize != this.frozenPileCopy.size()) {
            this.calculateScrollBounds();
        }
        this.resetScrolling();
        this.updateBarPosition();
    }

    private void calculateScrollBounds() {
        if (this.frozenPileCopy.size() > 10) {
            int scrollTmp = this.frozenPileCopy.size() / CARDS_PER_LINE - 2;
            if (this.frozenPileCopy.size() % CARDS_PER_LINE != 0) {
                ++scrollTmp;
            }
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * FrozenPileViewScreen.padY;
        } else {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }
        this.prevDeckSize = this.frozenPileCopy.size();
    }

    private void resetScrolling() {
        if (this.currentDiffY < this.scrollLowerBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        } else if (this.currentDiffY > this.scrollUpperBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }
    }

    private void updatePositions() {
        this.hoveredCard = null;
        int lineNum = 0;
        final ArrayList<AbstractCard> cards = this.frozenPileCopy.group;
        for (int i = 0; i < cards.size(); ++i) {
            final int mod = i % CARDS_PER_LINE;
            if (mod == 0 && i != 0) {
                ++lineNum;
            }
            cards.get(i).target_x = drawStartX + mod * FrozenPileViewScreen.padX;
            cards.get(i).target_y = drawStartY + this.currentDiffY - lineNum * padY;
            cards.get(i).update();
            cards.get(i).updateHoverLogic();
            if (cards.get(i).hb.hovered) {
                this.hoveredCard = cards.get(i);
            }
        }
    }

    public void reopen() {
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
    }

    public void open() {
        CardCrawlGame.sound.play("DECK_OPEN");
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
        this.currentDiffY = 0.0f;
        this.grabStartY = 0.0f;
        this.grabbedScreen = false;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = FrozenPileEnums.FROZEN_VIEW;
        this.frozenPileCopy.clear();
        for (final AbstractCard c : FrozenPileManager.frozenPile.group) {
            final AbstractCard toAdd = c.makeStatEquivalentCopy();
            toAdd.setAngle(0.0f, true);
            toAdd.targetDrawScale = 0.75f;
            toAdd.targetDrawScale = 0.75f;
            toAdd.drawScale = 0.75f;
            toAdd.lighten(true);
            this.frozenPileCopy.addToBottom(toAdd);
        }
        this.frozenPileCopy.sortAlphabetically(true);
        this.frozenPileCopy.sortByRarityPlusStatusCardType(true);
        this.hideCards();
        if (this.frozenPileCopy.group.size() <= CARDS_PER_LINE) {
            FrozenPileViewScreen.drawStartY = Settings.HEIGHT * 0.5f;
        } else {
            FrozenPileViewScreen.drawStartY = Settings.HEIGHT * 0.66f;
        }
        this.calculateScrollBounds();
    }

    private void hideCards() {
        int lineNum = 0;
        final ArrayList<AbstractCard> cards = this.frozenPileCopy.group;
        for (int i = 0; i < cards.size(); ++i) {
            final int mod = i % CARDS_PER_LINE;
            if (mod == 0 && i != 0) {
                ++lineNum;
            }
            cards.get(i).current_x = FrozenPileViewScreen.drawStartX + mod * FrozenPileViewScreen.padX;
            cards.get(i).current_y = FrozenPileViewScreen.drawStartY + this.currentDiffY - lineNum * FrozenPileViewScreen.padY - MathUtils.random(100.0f * Settings.scale, 200.0f * Settings.scale);
            cards.get(i).targetDrawScale = 0.75f;
            cards.get(i).drawScale = 0.75f;
        }
    }

    public void render(final SpriteBatch sb) {
        if (this.hoveredCard == null) {
            this.frozenPileCopy.render(sb);
        } else {
            this.frozenPileCopy.renderExceptOneCard(sb, this.hoveredCard);
            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.render(sb);
            this.hoveredCard.renderCardTip(sb);
        }
        FontHelper.renderDeckViewTip(sb, FrozenPileViewScreen.DESC, 96.0f * Settings.scale, Settings.CREAM_COLOR);
        if (this.shouldShowScrollBar()) {
            this.scrollBar.render(sb);
        }
    }

    @Override
    public void scrolledUsingBar(final float newPercent) {
        this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.updateBarPosition();
    }

    private void updateBarPosition() {
        final float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return this.scrollUpperBound > FrozenPileViewScreen.SCROLL_BAR_THRESHOLD;
    }

}