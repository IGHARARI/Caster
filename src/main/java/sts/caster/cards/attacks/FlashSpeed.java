package sts.caster.cards.attacks;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ElectrifyCardsAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class FlashSpeed extends CasterCard {

    public static final String ID = CasterMod.makeID("FlashSpeed");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("flashspeed.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 1;
    private static final int NUM_HITS = 4;
    private static final int UPGRADE_NUM_HITS = 2;
    private static final int BLOCK = 2;
    private static final int ELEC_AMOUNT = 1;

    public FlashSpeed() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = NUM_HITS;
        m2 = baseM2 = ELEC_AMOUNT;
        block = baseBlock = BLOCK;
        setCardElement(MagicElement.THUNDER);
    }


    private final AttackEffect[] availableEffects = {
            AttackEffect.SLASH_HORIZONTAL,
            AttackEffect.SLASH_VERTICAL,
            AttackEffect.SLASH_DIAGONAL,
            AttackEffect.BLUNT_LIGHT
    };

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            AttackEffect effect = availableEffects[AbstractDungeon.miscRng.random(0,3)];
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect, true));
        }
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ElectrifyCardsAction(m2, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_NUM_HITS);
            initializeDescription();
        }
    }
}
