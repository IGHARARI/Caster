package sts.caster.cards.attacks;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.HashSet;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.RepeatingRandomDamageAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

public class GateOfBabylon extends CasterCard {

    public static final String ID = CasterMod.makeID("GateOfBabylon");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("lazer.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 3;
    private static final int UPGR_DAMAGE = 2;

    public GateOfBabylon() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = BASE_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	int attacks = countSpellsInMasterDeck();
    	if (attacks > 0) {
    		AbstractMonster randomTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
    		AbstractDungeon.actionManager.addToBottom(new RepeatingRandomDamageAction(randomTarget, new DamageInfo(p,damage), attacks));
    	}
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGR_DAMAGE);
            initializeDescription();
        }
    }
    
    public static int countSpellsInMasterDeck() {
    	HashSet<String> spellIDs = new HashSet<String>();
    	for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
    		if (c.hasTag(TheCaster.Enums.DELAYED_CARD)) spellIDs.add(c.cardID);
    	}
    	return spellIDs.size();
    }
}
