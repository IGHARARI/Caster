package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

public class FrostDriver extends CasterCard {

    public static final String ID = CasterMod.makeID("FrostDriver");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("frostdriver.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_FROST = 3;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;
    private static final int BASE_BLOCK = 5;
    private static final int UPG_BLOCK = 2;


    public FrostDriver() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_FROST;
        baseSpellBlock = spellBlock = BASE_BLOCK;
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        baseDelayTurns = delayTurns = BASE_DELAY;
        this.tags.add(TheCaster.Enums.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, spellBlock));
    	DamageAction damageAction = new DamageAction(m, new DamageInfo(p, spellDamage), AttackEffect.BLUNT_HEAVY);
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, damageAction));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(UPG_DAMAGE);
            upgradeSpellBlock(UPG_BLOCK);
        }
    }
}
