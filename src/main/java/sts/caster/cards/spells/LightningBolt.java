package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.LightningDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class LightningBolt extends CasterCard {

    public static final String ID = CasterMod.makeID("LightningBolt");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("lightningbolt.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int DELAY_TURNS = 1;
    private static final int BASE_DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int DRAW_AMOUNT = 1;
    private static final int UPG_DRAW_AMOUNT = 1;


    public LightningBolt() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = DRAW_AMOUNT;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new LightningDamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageType.NORMAL), AttackEffect.SLASH_VERTICAL));
            actionsList.add(new DrawCardAction(AbstractDungeon.player, c.magicNumber));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPG_DRAW_AMOUNT);
        }
    }
}
