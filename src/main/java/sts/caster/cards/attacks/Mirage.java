package sts.caster.cards.attacks;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RetainOnceCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.core.freeze.IgnitedHelper;

import static sts.caster.core.CasterMod.makeCardPath;

public class Mirage extends CasterCard {

    public static final String ID = CasterMod.makeID("Mirage");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("mirage.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int DAMAGE_LOSS_ON_USE = 1;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Mirage() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = DAMAGE_LOSS_ON_USE;
        m2 = 0;
        returnToHand = true;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = baseDamage;
        baseDamage -= m2;
        super.applyPowers();
        if (m2 > 0) {
            isDamageModified = true;
        }
        baseDamage = realBaseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = baseDamage;
        baseDamage -= m2;
        super.calculateCardDamage(mo);
        if (m2 > 0) {
            isDamageModified = true;
        }
        baseDamage = realBaseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(IgnitedHelper.buildSelectCardsToIgniteAction(1));
		addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AttackEffect.FIRE));
        AbstractCard thisCard = this;
		addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!CardModifierManager.hasModifier(thisCard, RetainOnceCardMod.ID)) {
                    CardModifierManager.addModifier(thisCard, new RetainOnceCardMod());
                }
                isDone= true;
            }
        });
        m2 += magicNumber;

    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        m2 = 0;
        CasterMod.logger.info("Mirage end of turn, m2 set to: " + m2);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
