package sts.caster.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class TempleEventRoom extends AbstractRoom {
	private EventRoom fakeRoom;
    public TempleEventRoom() {
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "?";
        this.mapImg = ImageMaster.MAP_NODE_EVENT;
        this.mapImgOutline = ImageMaster.MAP_NODE_EVENT_OUTLINE;
        fakeRoom = new EventRoom();
    }
	
    @Override
    public void onPlayerEntry()
    {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        event = fakeRoom.event = new TempleEvent();
        fakeRoom.event.onEnterRoom();
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll)
    {
        return fakeRoom.getCardRarity(roll);
    }

    @Override
    public void update()
    {
        fakeRoom.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        fakeRoom.render(sb);
        fakeRoom.renderEventTexts(sb);
    }

    @Override
    public void renderAboveTopPanel(SpriteBatch sb)
    {
        fakeRoom.renderAboveTopPanel(sb);
    }

}
