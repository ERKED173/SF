package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.controllers.SFIn;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class InformationScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SFIn controller;
	
	private int iter;
	private float border;
	private Sprite texture;
	private Sprite picture;
	private Screen screen;
	
	private SFButtonS back;
	
	private static SFFont text;
	private String str;
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private ru.erked.spaceflight.Data data;
	
	public InformationScreen(final StartSFlight game, int iter, Screen screen){
		this.game = game;
		this.iter = iter;
		this.screen = screen;
		data = game.data;
	}
	
	@Override
	public void show() {
		
		controller = new SFIn();
	
		switch(iter){
		case 1:{
			picture = RES.atlas.createSprite("information1");
			picture.setBounds(0.8F*width, 0.15F*height, 0.125F*width, 0.44479F*width);
			if(!INF.lngRussian){
				str = game.ts.get("text.information.1");
				border = 0.6F;
			}else{
				str = game.ts.get("text.information.1");
				border = 0.6F;
			}
			break;
		}/**case 2:{
			picture = RES.atlas.createSprite("information2");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.29125F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"Outer space ","- ","it ","is ","mostly ","empty ","parts ","of ","the ","Universe",
						"outside ","of ","planets ","and ","other ","space ","objects. ","Many ","people ",
						"think ","that ","space ","is ","empty ","but ","it ","is ","incorrect.",
						"There ","are ","a ","few ","little ","particles ","and ","electromagnetic ","radiation."
				};
			}else{
				str = new String[]{
						"Космос ","- ","это ","почти ","пустые ","участки ","Вселенной ",
						"вне ","планет ","и ","других ","космических ","тел. ",
						"Многие ","думают, ","что ","космос ","- ","это ",
						"пустое ","пространство, ","однако ","это ","не ","так: ",
						"в ","нём ","есть ","очень ","маленькие ","частицы ",
						"и ","электромагнитное ","излучение."
				};
			}
			border = 0.75F;
			break;
		}case 3:{
			picture = RES.atlas.createSprite("information3");
			picture.setBounds(0.2F*width, 0.0F*height, 0.6F*width, 0.38074F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"Our ","Sun's ","weigth ","is ","1 989 100 ","000 ","000 ","000 ","000 ","000 ","000 ",
						"000 ","000 ","kilogramms ","(1,9891 nonillion). ","It ","is ","99,8% ","of ","mass ","of ",
						"Solar ","system."
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Наше ","Солнце ","весит ","1 989 100 ","000 ","000 ","000 ","000 ","000 ","000 ",
						"000 ","000 ","килограмм ","(1,9891 нониллиона). ","Это ","составляет ","99,8% ","всей ","массы ","Солнечной ",
						"системы."
				};
				border = 0.8F;
			}
			break;
		}case 4:{
			picture = RES.atlas.createSprite("information4");
			picture.setBounds(0.35F*width, 0.05F*height, 0.3F*width, 0.3F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"The ","Moon ","- ","Earth's ","only ","permanent ","natural ","satellite, ",
						"the ","second-brightest ","object ","on ","the ","night ","sky, ",
						"the ","fifth-largest ","natural ","satellite ","in ","Solar ","system. ",
						"Moon ","is ","only ","one ","space ","object ","where ","was a ","human."
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Луна ","- ","спутник ","Земли, ","второй ","по ",
						"яркости ","объект ","на ","ночном ","небе, ","пятый ","по ",
						"величине ","спутник ","в ","Солнечной ","системе. ","Луна ",
						"является ","единственным ","космическим ","объектом, ","на ","котором ",
						"побывал ","человек."
				};
				border = 0.775F;
			}
			break;
		}case 5:{
			picture = RES.atlas.createSprite("information5");
			picture.setBounds(0.35F*width, 0.05F*height, 0.3F*width, 0.3F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"Jupiter ","is ","so ","big ","that ","it ",
						"can ","accomodate ","all ","the ","other ","planets ","in ",
						"the ","Solar ","system."
				};
				border = 0.85F;
			}else{
				str = new String[]{
						"Юпитер ","настолько ","большой, ","что ","внутри ","него ",
						"могут ","поместиться ","все ","остальные ","планеты ","Солнечной ","системы."
				};
				border = 0.75F;
			}
			break;
		}case 6:{
			picture = RES.atlas.createSprite("information7");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.333125F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"In ","the ","Solar ","system ","are ","hundreds ",
						"of ","thousands ","of ","the ","asteroids. ","Think ","why ",
						"asteroid ","fall ","on ","the ","ground ","is ","very ",
						"rare? ","(Asteroid ","which ","fell ","on ","the ",
						"ground ","- ","it's a ","meteorite)."
				};
				border = 0.85F;
			}else{
				str = new String[]{
						"В ","Солнечной ","системе ","на ","данный ","момент ",
						"обнаружены ","сотни ","тысяч ","астероидов. ","Подумайте, ","почему ",
						"на ","нашу ","планету ","так ","редко ","падают ","астероиды? ","(Астероид, ",
						"упавший ","на ","землю, ","называется ","метеорит)."	
				};
				border = 0.8F;
			}
			break;
		}case 7:{
			picture = RES.atlas.createSprite("information6");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.35333F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"The ","comet ","is ","small ","space ","object ",
						"which ","revolves ","around ","the ","Sun ","in ","an ",
						"elongated ","orbit. ","When ","approaching ","the ","Sun ",
						"in ","the ","comet ","appears ","two ","tails ","from ",
						"the ","dust ","or ","the ","gas."
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Комета ","- ","это ","небольшое ","небесное ","тело, ",
						"которое ","обращается ","вокруг ","Солнца ","по ","довольно ",
						"вытянутой ","орбите. ","При ","приближении ","кометы ","к Солнцу ",
						"у ","неё ","появляется ","хвосты ","- ","смеси ","газа ","или ","пыли,",
						"направленные ","от ","Солнца. "
						
				};
				border = 0.75F;
			}
			break;
		}case 8:{
			picture = RES.atlas.createSprite("information8");
			picture.setBounds(0.5F*width, 0.15F*height, 0.5F*width, 0.4F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"Black ","hole ","is ","a ","space ","object ","that ","has ","so ",
						"huge ","mass ","that ","even ","light ","can not ","overcome ","its ","gravitation. ",
						"The ","border ","of ","black ","hole ","is ","an ","'event ",
						"horizon' ","and ","the ","size ","of it ","is ","'schwarzschild ","radius'. ","Black ",
						"holes ","form ","from ","superhuge ","stars ","or ","into ","centres ","of ",
						"a galaxies."
				};
				border = 0.3F;
			}else{
				str = new String[]{
						"Чёрная дыра ","- ","это ","космический ","объект, ","у которого ",
						"настолько ","большая ","масса, ","что ","даже ","свет ",
						"не ","может ","преодолеть ","его ","гравитацию. ","Граница ","чёрной ",
						"дыры ","называется ","'горизонт ","событий', ","а ","размер ","- ",
						"'гравитационный ","радиус'. ","Чёрные ","дыры ","образуются ","из ",
						"сверхмассивных ","звёзд ","или ","в центре ","галактики. "
						
				};
				border = 0.4F;
			}
			break;
		}case 9:{
			picture = RES.atlas.createSprite("information9");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.32535F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"The temperature ","on ","the ","Earth's ","orbit ","is ",
						"+4 ","degrees ","Celsius. ","However, ","in ","the ","our ",
						"planet ","shadow ","temperature ","is ","-160 ","degrees ",
						"Celsius. ","Average ","temperature ","into ","the ","space ","is ",
						"-270,15 ","degrees","Celsius. "
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Температура ","на ","орбите ","Земли ","составляет ","+4 ","градуса ",
						"по ","Цельсию. ","Однако ","в ","тени ","нашей ",
						"планеты ","температура ","составляет ","-160 ","градусов ","по Цельсию. ",
						"Средняя ","температура ","во ","всём ","космосе ","составляет ","-270,15 ","градусов ",
						"Цельсия. "
						
				};
				border = 0.75F;
			}
			break;
		}case 10:{
			picture = RES.atlas.createSprite("information10");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.41857F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"Think ","why ","astronauts ","have ","to ","train ",
						"a lot ","before ","and ","after ","the ","space ","flight? "
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Подумайте, ","почему ","космонавтам ","приходится ","много ","тренироваться ",
						"до ","космического ","полёта ","и ","после? "
				};
				border = 0.75F;
			}
			break;
		}case 11:{
			picture = RES.atlas.createSprite("information11");
			picture.setBounds(0.5F*width, 0.25F*height, 0.5F*width, 0.35333F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"The ","ISS ","(International ","Space ","Station) ","is ","a ","piloted ","orbital ","station.",
						"The station ","was ","collected ","into ","the ","space, ","in other ","words ",
						"ISS ","was ","biult ","on the ","planet. ","The first ","module ","of ","station ","was ",
						"put into ","orbit ","in ","1998 ","by Russia. ","At the ","station ","always ","live ","austronauts. ",
						"They ","are ","condcting ","important ","for ","science ","experiments ","in ","the ",
						"conditions ","of ","an ","open ","space."
				};
				border = 0.4F;
			}else{
				str = new String[]{
						"МКС - ","Международная ","Космическая ","Станция - ","это ","пилотируемая ",
						"орбитальная ","станция. ","Станция ","модульная, ","то есть ","состоит ","из ","различных ",
						"частей. ","МКС ","была ","собрана ","в ","космосе, ","а не ","на ","Земле. ",
						"Первый ","модуль ","станции ","был ","выведен ","в ","космос ","Россией ","в 1998 году. ",
						"На ","станции ","постоянно ","живут ","космонавты. ","Они ","проводят ","важные ",
						"для ","науки ","эксперименты ","в условиях ","открытого ","космоса. "
						
				};
				border = 0.4F;
			}
			break;
		}case 12:{
			picture = RES.atlas.createSprite("information12");
			picture.setBounds(0.15F*width, 0.0F*height, 0.7F*width, 0.24708F*(7.0F/5.0F)*width);
			if(!INF.lngRussian){
				str = new String[]{
						"The nebula ","is ","a ","huge ","accumulation ","of ","dust, ","gase ","or ","plasma. ",
						"Often ","nebulas ","are ","galaxies. ","Nebula ","forms ","from ","the ","'supernova' ",
						"(destructed ","superhuge ","star)."
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Туманность - ","это ","огромные ","скопления ","пыли, ","газа ","или ","плазмы. ",
						"Нередко ","туманности ","являются ","галактиками. ","Туманности ","образуются ","из ",
						"остатков ","'сверхновых' ","(взовравшихся ","сверхмассивных ","звёзд). ",
						
				};
				border = 0.75F;
			}
			break;
		}case 13:{
			picture = RES.atlas.createSprite("information13");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.35333F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"One ","spoon ","of ","neutron ","star ","substanse ","is ","weight ","150 millions ","of tons. ",
						"In other ","words, ","if ","you ","fill ","a ","barrel ","with this ","substance, ",
						"th mass of ","this ","barrel ","will be ","the same ","as ","mass of ","natural ","satellite ","of Mars - ","Phobos."
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Одна ","ложка ","вещества ","нейтронной ","звезды ","весит ","150 миллионов ","тонн. ",
						"Другими словами, ","если ","заполнить ","обычную ","бочку ","этим ","веществом, ","то ","эта ","бочка ","будет ","весить, ",
						"как ","спутник ","Марса - ","Фобос."
						
				};
				border = 0.75F;
			}
			break;
		}case 14:{
			picture = RES.atlas.createSprite("information14");
			picture.setBounds(0.15F*width, 0.0F*height, 0.7F*width, 0.4375F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"Only ","one ","quasar ","brighter ","than ","all ","of ","the ","stars ","of ","Milky ","Way."
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Всего ","лишь ","один ","квазар ","ярче, ","чем ","все ","вместе ","взятые ","звёзды ","Млечного ","Пути."
				};
				border = 0.75F;
			}
			break;
		}case 15:{
			picture = RES.atlas.createSprite("information15");
			picture.setBounds(0.25F*width, 0.0F*height, 0.5F*width, 0.3625F*width);
			if(!INF.lngRussian){
				str = new String[]{
						"If ","one ","of ","two ","twins ","flies ","on ","the ","rocket ","in ","other ",
						"galaxy ","and ","after ","return ","to ","the ","first ","twin ","then ",
						"twin ","that ","was ","on ","the ","planet ","will ","be ","older ","than ","twin",
						"that ","was ","in ","the ","flight. ","Why?"
				};
				border = 0.8F;
			}else{
				str = new String[]{
						"Если ","один ","близнец ","улетит ","на ","ракете ","в ","другую ",
						"галактику, ","а ","потом ","вернётся ","к ","своему ","близнецу, ",
						"то близнец, ","который ","остался ","на ","Земле ","будет ","старше, ",
						"чем тот, ","который ","прилетел ","обратно. ","Почему?"
				};
				border = 0.75F;
			}
			break;
		}default:{

		}
		*/
		}
		
		MainMenu.music.play();
		
		texture = RES.atlas.createSprite("info");
		texture.setBounds(0.0F, 0.0F, width, height);
		texture.setColor(Color.GRAY);
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}

		text = new SFFont(30, Color.WHITE);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		resourcesCheck();
		
		if(alp>0.0F && (!isTrans)){
			blackAlpha.setAlpha(alp);
			alp-=0.05F;
		}else if(!isTrans){
			blackAlpha.setAlpha(0.0F);
			alp = 0.0F;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		texture.draw(game.batch);
		picture.draw(game.batch);
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);
		
		textDraw();
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			game.setScreen(screen);
			this.dispose();
		}
		
	}
	
	private void textDraw(){
		switch(iter){
		case 1:{
			if(!INF.lngRussian){
				text.draw(game.batch, "Launch vehicle", 0.5F*(width-text.getWidth("Launch vehicle")), 0.965F*height);
			}else{
				text.draw(game.batch, "Ракета-носитель", 0.5F*(width-text.getWidth("Ракета-носитель")), 0.965F*height);
			}
			break;
		}case 2:{
			if(!INF.lngRussian){
				text.draw(game.batch, "Outer space", 0.5F*(width-text.getWidth("Outer space")), 0.965F*height);
			}else{
				text.draw(game.batch, "Космическое пространство", 0.5F*(width-text.getWidth("Космическое пространство")), 0.965F*height);
			}
			break;
		}case 4:{
			if(!INF.lngRussian){
				text.draw(game.batch, "The Moon", 0.5F*(width-text.getWidth("The Moon")), 0.965F*height);
			}else{
				text.draw(game.batch, "Луна", 0.5F*(width-text.getWidth("Луна")), 0.965F*height);
			}
			break;
		}case 7:{
			if(!INF.lngRussian){
				text.draw(game.batch, "The comet", 0.5F*(width-text.getWidth("The comet")), 0.965F*height);
			}else{
				text.draw(game.batch, "Комета", 0.5F*(width-text.getWidth("Комета")), 0.965F*height);
			}
			break;
		}case 8:{
			if(!INF.lngRussian){
				text.draw(game.batch, "Black hole", 0.5F*(width-text.getWidth("Black hole")), 0.965F*height);
			}else{
				text.draw(game.batch, "Чёрная дыра", 0.5F*(width-text.getWidth("Чёрная дыра")), 0.965F*height);
			}
			break;
		}case 11:{
			if(!INF.lngRussian){
				text.draw(game.batch, "ISS", 0.5F*(width-text.getWidth("ISS")), 0.965F*height);
			}else{
				text.draw(game.batch, "МКС", 0.5F*(width-text.getWidth("МКС")), 0.965F*height);
			}
			break;
		}case 12:{
			if(!INF.lngRussian){
				text.draw(game.batch, "Nebula", 0.5F*(width-text.getWidth("Nebula")), 0.965F*height);
			}else{
				text.draw(game.batch, "Туманность", 0.5F*(width-text.getWidth("Туманность")), 0.965F*height);
			}
			break;
		}case 3:case 5:case 9:case 13:case 14:{
			if(!INF.lngRussian){
				text.draw(game.batch, "Did you know?", 0.5F*(width-text.getWidth("Did you know?")), 0.965F*height);
			}else{
				text.draw(game.batch, "А вы знали?", 0.5F*(width-text.getWidth("А вы знали?")), 0.965F*height);
			}
			break;
		}case 6:case 10:case 15:{
			if(!INF.lngRussian){
				text.draw(game.batch, "There is something to think about", 0.5F*(width-text.getWidth("There is something to think about")), 0.965F*height);
			}else{
				text.draw(game.batch, "Есть, над чем подумать", 0.5F*(width-text.getWidth("Есть, над чем подумать")), 0.965F*height);
			}
			break;
		}default:{
			break;
		}
		}
        int j = 0;
        float marginLeft = 0.0F;
        float marginTop = 0.0F;
        String sP = "";
		for(int i=0;i<str.length();i++) {
            String s = "";
			if(str.substring(i, i+1).equals(" ")){
                s = str.substring(j, i);
                if(j != 0) {
                    if (marginLeft + text.getWidth(sP + " ") < border * width) {
                        marginLeft += text.getWidth(sP + " ");
                    } else {
                        marginLeft = 0.0F;
                        marginTop += 1.5F * text.getHeight("A");
                    }
                }
                j = i+1;
                sP = s;
			}
            text.draw(game.batch, s, 0.02F*width + marginLeft, 0.875F*height - marginTop);
		}
	}
	
	private void resourcesCheck(){
		if(INF.money>INF.moneyFull) INF.money = INF.moneyFull;
		if(INF.fuel>INF.fuelFull) INF.fuel = INF.fuelFull;
		if(INF.metal>INF.metalFull) INF.metal = INF.metalFull;
	}
	
	@Override
	public void resize(int width, int height) {

	}
	@Override
	public void pause() {
		data.saveSF();
	}
	@Override
	public void resume() {
		MainMenu.music.play();
		data.loadSF();
	}
	@Override
	public void hide() {
		data.saveSF();
	}

	@Override
	public void dispose() {
		data.saveSF();
		game.dispose();
		text.dispose();
	}

}