// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.util.ArrayList;

public class StringUtils
{
    private static StringUtils instance;
    public String CLIENT_NAME;
    public String CLIENT_TITLE;
    public String RealLiveNames;
    
    static {
        StringUtils.instance = new StringUtils();
    }
    
    public StringUtils() {
        this.RealLiveNames = "Abigail,Alexandra,Alison,Amanda,Amelia,Amy,Andrea,Angela,Anna,Anne,Audrey,Ava,Bella,Bernadette,Carol,Caroline,Carolyn,Chloe,Claire,Deirdre,Diana,Diane,Donna,Dorothy,Elizabeth,Ella,Emily,Emma,Faith,Felicity,Fiona,Gabrielle,Grace,Hannah,Heather,Irene,Jan,Jane,Jasmine,Jennifer,Jessica,Joan,Joanne,Julia,Karen,Katherine,Kimberly,Kylie,Lauren,Leah,Lillian,Lily,Lisa,Madeleine,Maria,Mary,Megan,Melanie,Michelle,Molly,Natalie,Nicola,Olivia,Penelope,Pippa,Rachel,Rebecca,Rose,Ruth,Sally,Samantha,Sarah,Sonia,Sophie,Stephanie,Sue,Theresa,Tracey,Una,Vanessa,Victoria,Virginia,Wanda,Wendy,Yvonne,Zoe,Adam,Adrian,Alan,Alexander,Andrew,Anthony,Austin,Benjamin,Blake,Boris,Brandon,Brian,Cameron,Carl,Charles,Christian,Christopher,Colin,Connor,Dan,David,Dominic,Dylan,Edward,Eric,Evan,Frank,Gavin,Gordon,Harry,Ian,Isaac,Jack,Jacob,Jake,James,Jason,Joe,John,Jonathan,Joseph,Joshua,Julian,Justin,Keith,Kevin,Leonard,Liam,Lucas,Luke,Matt,Max,Michael,Nathan,Neil,Nicholas,Oliver,Owen,Paul,Peter,Phil,Piers,Richard,Robert,Ryan,Sam,Sean,Sebastian,Simon,Stephen,Steven,Stewart,Thomas,Tim,Trevor,Victor,Warren,William,Abraham,Allan,Alsop,Anderson,Arnold,Avery,Bailey,Baker,Ball,Bell,Berry,Black,Blake,Bond,Bower,Brown,Buckland,Burgess,Butler,Cameron,Campbell,Carr,Chapman,Churchill,Clark,Clarkson,Coleman,Cornish,Davidson,Davies,Dickens,Dowd,Duncan,Dyer,Edmunds,Ellison,Ferguson,Fisher,Forsyth,Fraser,Gibson,Gill,Glover,Graham,Grant,Gray,Greene,Hamilton,Hardacre,Harris,Hart,Hemmings,Henderson,Hill,Hodges,Howard,Hudson,Hughes,Hunter,Ince,Jackson,James,Johnston,Jones,Kelly,Kerr,King,Knox,Lambert,Langdon,Lawrence,Lee,Lewis,Lyman,MacDonald,Mackay,Mackenzie,MacLeod,Manning,Marshall,Martin,Mathis,May,McDonald,McLean,McGrath,Metcalfe,Miller,Mills,Mitchell,Morgan,Morrison,Murray,Nash,Newman,Nolan,North,Ogden,Oliver,Paige,Parr,Parsons,Paterson,Payne,Peake,Peters,Piper,Poole,Powell,Pullman,Quinn,Rampling,Randall,Rees,Reid,Roberts,Robertson,Ross,Russell,Rutherford,Sanderson,Scott,Sharp,Short,Simpson,Skinner,Slater,Smith,Springer,Stewart,Sutherland,Taylor,Terry,Thomson,Tucker,Turner,Underwood,Vance,Vaughan,Walker,Wallace,Walsh,Watson,Welch,White,Wilkins,Wilson,Wright,YoungAbram,Abakumov,Afanas,Abdulov,Afanas,Abramov,Afanasy,Abramovich,Afon,Afanasyev,Afonasei,Aksyonov,Akim,Alexandrov,Albert,Alexeyev,Aleks,Andreyev,Alexander,Anisimov,Alexei,Artyomov,Anatoly,Avdeyev,Andrei,Averyanov,Anton,Balabanov,Arkady,Balandin,Arseny,Bazhenov,Artur,Bazin,Artyom,Belinsky,Bogatyr,Bezrukov,Bogdan,Blokhin,Boleslav,Bobkov,Boris,Bobrov,Daniil,Bogdanov,David,Bogolyubov,Denis,Bogomazov,Dmitry,Bychkov,Dobrushin,Chkalov,Dorofey,Davydov,Eduard,Dementyev,Erik,Demidov,Evgeniy,Denisov,Evgeny,Dmitriyev,Faddei,Dostoyevsky,Fanasiy,Dubinin,Fedir,Engalychev,Filipp,Essen,Florentiy,Fokin,Foma,Fomin,Foma,Frolov,Garry,Gagarin,Gavriil,Garin,Gavril,Gerasimov,Gedeon,Glazkov,Gennady,Ibragimov,Georgy,Ignatyev,Gerasim,Ilyin,German,Isayev,Gleb,Istomin,Goga,Ivankov,Grigory,Ivanov,Iakov,Ivashov,Ignat,Izmaylov,Igor,Kalashnik,Ilariy,Kalinin,Ilia,Kapitsa,Illarion,Kapustin,Ilya,Kazakov,Immanuil,Kazantsev,Innokentiy,Khabarov,Ioakim,Kirillov,Ioann,Kirilov,Iosif,Knyazev,Ipatiy,Kolesov,Ippolit,Koltsov,Irinei,Komarov,Isai,Korotkov,Isidor,Koshkin,Ivan,Kostin,Julij,Kotov,Kazimir,Kovalevsky,Khariton,Kozlovsky,Kir,Kravchuk,Kirill,Kruglov,Kliment,Krylov,Koldan,Krymov,Kolmogorov,Kryukov,Kolodka,Kudryashov,Kolya,Kudryavtsev,Kolzak,Kutepov,Konstantin,Kutuzov,Kostya,Kuzmin,Kuzma,Kuznetsov,Lavrentii,Kvasov,Lazar,Lapidus,Leonid,Lapin,Leontiy,Larionov,Ludmil,Lavrentyev,Makar,Lavrov,Makariy,Lebedev,Maksim,Lobanov,Marat,Loginov,Mark,Loginovsky,Marlen,Loktionov,Matvei,Lomonosov,Matvey,Losev,Maxim,Lukin,Mefodiy,Lukyanenko,Melor,Lytkin,Mikhail,Makarov,Mikhail,Maksimov,Miron,Malakhov,Misha,Markov,Mitrofan,Matveyev,Mitya,Myasnikov,Modest,Myshkin,Modya,Naumov,Motya,Nazarov,Naum,Nekrasov,Nazar,Nesterov,Nazariy,Nikiforov,Nestor,Nikitin,Nikita,Nikolayev,Nikolay,Nikonov,Oleg,Noskov,Onisim,Nosov,Osip,Novikov,Pankratiy,Obolensky,Pasha,Orlov,Patya,Ostrovsky,Pavel,Ozerov,Petya,Pavlov,Pyotr,Pestov,Peter,Petrov,Robert,Polivanov,Rodion,Polunin,Rodya,Ponomaryov,Rolan,Popov,Roman,Preobraz,Rostislav,Primakov,Rostya,Pugin,Rurik,Putin,Ruslan,Rabinovich,Samuil,Sakharov,Sasha,Samoylov,Sashura,Samsonov,Saveliy,Sapozhnikov,Savin,Sedov,Savva,Seleznyov,Semyon,Semyonov,Sergei,Sergeyev,Spartak,Severny,Stanislav,Shalyapin,Stas,Shaposhnikov,Stefan,Sharapov,Stepan,Sharov,Taras,Sidorov,Timofei,Solomin,Timur,Sorokin,Timur,Stalin,Tit,Stepanov,Trofim,Sukhanov,Vadim,Sychyov,Valentin,Syomin,Valeriy,Sysoyev,Valery,Tamarkin,Vanja,Tarasov,Vanya,Tereshchenko,Varfolomei,Timofeyev,Varnava,Titov,Vasiliy,Tsereteli,Vasily,Tsulukidze,Vasya,Tsvetkov,Venedikt,Turov,Veniamin,Ukhtomsky,Vikentiy,Ulanov,Viktor,Ulyanov,Vitaliy,Uspensky,Vitaly,Ustinov,Vitya,Uvarov,Vlad,Vasilyev,Vladimir,Vavilov,Vladimir,Vereshchagin,Vladislav,Vinogradov,Vlasii,Volkov,Vlasiy,Volodin,Volya,Voloshin,Vsevolod,Vorobyov,Vyacheslav,Voronin,Yakim,Voronov,Yakov,Yablonsky,Yaromir,Yakimov,Yaropolk,Yakovlev,Yaroslav,Yakubovich,Yasha,Yakushev,Yefim,Yashin,Yefrem,Yegorov,Yegor,Yeltsin,Yemelyan,Yevdokimov,Yermolai,Yevseyev,Yevgeniy,Yusupov,Yuli,Zaporozhets,Yulian,Zarubin,Yuliy,Zaytsev,Yuri,Zhdanov,Zakhar,Zimin,Zinoviy,Zubarev,Ben,Paul,Jonas,Elias,Leon,Fynn,Noah,Louis,Lucas,Felix,Luca,Maximilian,Henry,Max,Oscar,Emil,Liam,Jacob,Moritz,AntonMia,Emma,Sophia,Hanna,Emilia,Anna,Marie,Mila,Lina,Lea,Lena,Leonie,Amelie,Luisa,Johanna,Emily,Clara,Sophie,Charlotte,Lilly,Andrea,Angelika,Anna,Christa,Elke,Emma,Erika,Gabriele,Gisela,Ilse,Ingrid,Karin,Katrin,Marie,Martina,Monika,Melanie,Nadine,Nicole,Petra,Sabine,Sabrina,Sandra,Stefanie,Susanne,Alexander,Andreas,Christian,Daniel,Dennis,Dieter,Ernst,Frank,Fritz,Hermann,J\u00f6rg,Kurt,Manfred,Martin,Otto,Paul,Sebastian,Wolfgang,Mia,Emma,Hannah,Sofia,Anna,Emilia,Lina,Marie,Lena,Mila,Emily,Lea,Leonie,Amelie,Sophie,Johanna,Luisa,Clara,Lilly,Laura,Nele,Lara,Charlotte,Leni,Maja,Frieda,Mathilda,Ida,Ella,Pia,Sarah,Lia,Lotta,Greta,Melina,Julia,Paula,Lisa,Marlene,Zoe,Alina,Victoria,Mira,Elisa,Isabella,Helena,Josephine,Mara,Isabell,Nora,Antonia,Lucy,Emely,Jana,Pauline,Amy,Anni,Merle,Finja,Katharina,Luise,Elena,Theresa,Annika,Luna,Romy,Maria,Stella,Fiona,Jasmin,Magdalena,Jule,Milena,Mina,Carla,Eva,Martha,Nina,Annabell,Melissa,Elina,Carlotta,Paulina,Maila,Elif,Elisabeth,Ronja,Zoey,Chiara,Tilda,Miriam,Franziska,Valentina,Juna,Linda,Thea,Elli,Rosalie,Selina,Fabienne";
    }
    
    public static StringUtils getStringUtils() {
        return StringUtils.instance;
    }
    
    public String getTextWithFirstCharUpperCase(final String text) {
        String FirstChar = new StringBuilder().append(text.charAt(0)).toString();
        final String workingText = text.replace(text.split(FirstChar)[0], "").substring(1);
        FirstChar = FirstChar.toUpperCase();
        final String doneText = String.valueOf(String.valueOf(FirstChar)) + workingText;
        return doneText;
    }
    
    public ArrayList<String> StringArrayToArrayList(final String[] array) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String listArray : array) {
            list.add(listArray);
        }
        return list;
    }
    
    public String editToBigSize(final String text, final int maxSize) {
        if (text.length() > maxSize) {
            final String workingString = text.substring(9);
            final String FinishedString = String.valueOf(String.valueOf(text.replace(workingString, ""))) + "...";
            return FinishedString;
        }
        return text;
    }
}
