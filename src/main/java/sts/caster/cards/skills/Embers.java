package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.actions.ThawCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

public class Embers extends CasterCard {

    public static final String ID = CasterMod.makeID("Embers");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("embers.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int UPG_COST = 0;
    private static final int THAW_AMOUNT = 2;
    private static final int UPG_THAW_AMOUNT = 1;


    public Embers() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = THAW_AMOUNT;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	int attackingMonsters = 0;
        for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
            AbstractMonster targetMonster = AbstractDungeon.getMonsters().monsters.get(i);
            if (targetMonster.intent == AbstractMonster.Intent.ATTACK ||
                    targetMonster.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                    targetMonster.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
                    targetMonster.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
                attackingMonsters++;
            }
        }
        addToBot(new GainEnergyAction(attackingMonsters));
        addToBot(new ThawCardAction(magicNumber, false, true));
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            upgradeMagicNumber(UPG_THAW_AMOUNT);
        }
    }
}
