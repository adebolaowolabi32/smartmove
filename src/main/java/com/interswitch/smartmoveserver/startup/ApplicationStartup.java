package com.interswitch.smartmoveserver.startup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.TicketTillView;
import com.interswitch.smartmoveserver.repository.SeatRepository;
import com.interswitch.smartmoveserver.repository.StateRepository;
import com.interswitch.smartmoveserver.repository.TicketTillRepository;
import com.interswitch.smartmoveserver.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @author adebola.owolabi
 */
@Slf4j
@Component
public class ApplicationStartup implements CommandLineRunner {
    @Autowired
    UserService userService;

    @Autowired
    ManifestService manifestService;

    @Autowired
    TripService tripService;

    @Autowired
    VehicleMakeService vehicleMakeService;

    @Autowired
    VehicleModelService vehicleModelService;

    @Autowired
    SeatRepository seatRepo;

    @Autowired
    StateRepository stateRepo;

   @Autowired
   TicketTillRepository ticketTillRepo;

   @Autowired
   PassportService passportService;


    @Override
    public void run(String... args) throws IOException {
        User adminUser = new User();
        adminUser.setFirstName("Smart");
        adminUser.setLastName("Move");
        adminUser.setUsername("smart.move13@interswitch.com");
        adminUser.setPassword("Password123$");
        adminUser.setEmail("smart.move13@interswitch.com");
        adminUser.setAddress("Lagos Nigeria");
        adminUser.setRole(Enum.Role.ISW_ADMIN);
        adminUser.setEnabled(true);
        userService.setUp(adminUser);
        log.info("System Administrator created successfully!");

        User driver = new User();
        adminUser.setFirstName("Suleiman");
        adminUser.setLastName("Adelabu");
        adminUser.setUsername("sule.adelabu");
        adminUser.setPassword("Password123$");
        adminUser.setEmail("earnest.suru@gmail.com");
        adminUser.setAddress("Lagos Nigeria");
        adminUser.setRole(Enum.Role.DRIVER);
        adminUser.setEnabled(true);
        userService.setUpS(adminUser);
        log.info("Driver created successfully!");
        //loadManifestData(7);
        loadStatesAndLocalGovt();
        loadVehicleMakesAndModels();
    }

/*
    public void loadManifestData(long tripId) {

        manifestService.deleteAll();
        seatRepo.deleteAll();

        Manifest manifest = new Manifest();
        manifest.setBoarded(true);
        manifest.setContactEmail("eb-things@gmail.com");
        manifest.setContactMobile("09029898722");
        manifest.setIdNumber("NG-2989876510");
        manifest.setGender("Male");
        manifest.setName("Joel Jacintha");
        manifest.setNationality("Nigeria");
        manifest.setNextOfKinMobile("08038972655");
        manifest.setNextOfKinName("Joel Anyanwu");
        manifest.setTrip(tripService.findById(tripId));
        Seat seat = new Seat();
        seat.setSeatId("01");
        seat.setRowNo(2);
        seat.setColumnNo(1);
        seatRepo.save(seat);
        manifest.setSeat(seat);
        manifest.setAddress("58 Jakande,Lagos");
        manifestService.save(manifest);
        //
        manifest = new Manifest();
        manifest.setBoarded(true);
        manifest.setContactEmail("ay@gmail.com");
        manifest.setContactMobile("08029898722");
        manifest.setIdNumber("NG-2989876500");
        manifest.setGender("Female");
        manifest.setName("Ken Wale");
        manifest.setNationality("Nigeria");
        manifest.setNextOfKinMobile("08030972655");
        manifest.setNextOfKinName("Ayomide Wale");
        manifest.setTrip(tripService.findById(tripId));
        seat = new Seat();
        seat.setSeatId("02");
        seat.setRowNo(2);
        seat.setColumnNo(2);
        seatRepo.save(seat);
        manifest.setSeat(seat);
        manifest.setAddress("22 Captain Black Road,Lagos.");
        manifestService.save(manifest);
    }
*/

    public void loadVehicleMakesAndModels(){
        Map<String, List<String>> vehicles = new HashMap<>();
        vehicles.put("Toyota", parseToList("Coaster, Hiace, Grand Hiace, Quantum"));
        vehicles.put("Innoson", parseToList("5000, 6601, 6857, 6730, 6850, 6751"));
        vehicles.put("Marco Polo", parseToList("Paradiso 1350, Ideale, Torino Express, Torino Low Entry, Torino (Rear Engine)"));

        for (Map.Entry<String, List<String>> entry : vehicles.entrySet()) {
            VehicleMake vehicleMake = new VehicleMake();
            vehicleMake.setName(entry.getKey());
            if (!vehicleMakeService.existsByNameIgnoreCase(vehicleMake.getName())){
                VehicleMake vehicleMake1 = vehicleMakeService.save(vehicleMake);
                for (String model : new ArrayList<>(entry.getValue())) {
                    VehicleModel vehicleModel = new VehicleModel();
                    vehicleModel.setName(model);
                    vehicleModel.setMake(vehicleMake1);
                    vehicleModelService.save(vehicleModel);
                }
            }
        }
    }

    public void loadStatesAndLocalGovt() {
        List<State> stateList = stateRepo.findAll();
        if (stateList.isEmpty() || stateList.size() <= 36) {
            Map<String, List<String>> stateToLocalGovtMap = new HashMap<>();

            stateToLocalGovtMap.put("Abia", parseToList(
                    "Aba North, Aba South, Arochukwu, Bende, Abia, Ikwuano, Isiala Ngwa North, Isiala Ngwa South, Isuikwuato, Ohafia, Osisioma Ngwa, Ugwunagbo, Ukwa East, Ukwa West, Umu Nneochi, Umuahia North, Umuahia South, Obi Ngwa"));
            stateToLocalGovtMap.put("Adamawa", parseToList(
                    "Demsa, Fufore, Ganye, Girei, Gombi, Guyuk, Hong, Jada, Lamurde, Madagali, Maiha, Mayo-Belwa, Michika, Mubi North, Mubi South, Numan, Shelleng, Song, Toungo, Yol (State capital), Jimeta,"));
            stateToLocalGovtMap.put("Akwa Ibom", parseToList(
                    "Abak, Eastern Obolo, Eket, Esit-Eket, Essien Udim, Etim-Ekpo, Etinan, Ibeno, Ibesikpo-Asutan, Ibiono-Ibom, Ika, Ikono, Ikot Abasi, Ikot Ekpene, Ini, Itu, Mbo, Mkpat-Enin, Nsit-Atai, Nsit-Ibom, Nsit-Ubium, Obot-Akara, Okobo, Onna, Oron, Oruk Anam, Ukanafun, Udung-Uko, Uruan, Urue-Offong/Oruko, Uyo"));
            stateToLocalGovtMap.put("Anambra", parseToList(
                    "Aguata, Awka North, Awka South, Anambra East, Anambra West, Anaocha, Ayamelum, Dunukofia, Ekwusigo, Idemili North, Idemili South, Ihiala, Njikoka, Nnewi North, Nnewi South, Ogbaru, Onitsha North, Onitsha South, Orumba North, Orumba South, Oyi"));
            stateToLocalGovtMap.put("Bauchi", parseToList(
                    "Bauchi, Tafawa Balewa, Dass, Toro, Bogoro, Ningi, Warji, Ganjuwa, Kirfi, Alkaleri, Southern region totals, Darazo, Misau, Giade, Shira, Jama'are, Katagum, Itas/Gadau, Zaki, Gamawa, Damban"));
            stateToLocalGovtMap.put("Bayelsa",
                    parseToList("Brass, Ekeremor, Kolokuma/Opokuma, Nembe, Ogbia, Sagbama, Southern Ijaw, Yenagoa"));
            stateToLocalGovtMap.put("Benue", parseToList(
                    "Ado, Agatu, Apa, Buruku, Gboko, Guma, Gwer East, Gwer West, Katsina-Ala, Konshisha, Kwande, Logo, Makurdi, Obi, Ogbadibo, Ohimini, Oju, Okpokwu, Otukpo, Tarka, Ukum, Ushongo, Vandeikya"));
            stateToLocalGovtMap.put("Borno", parseToList(
                    "Maiduguri, Ngala, Kala/Balge, Mafa, Konduga, Bama, Jere, Dikwa, Askira/Uba, Bayo, Biu, Chibok, Damboa, Gwoza, Hawul, Kwaya, Kusar, Shani, Abadam, Gubio, Guzamala, Kaga, Kukawa, Magumeri, Marte, Mobbar, Monguno, Nganzai"));
            stateToLocalGovtMap.put("Cross River", parseToList(
                    "Abi, Akamkpa, Akpabuyo, Bekwarra, Bakassi, Biase, Boki, Calabar Municipal, Calabar South, Etung, Ikom, Obanliku, Obubra, Obudu, Odukpani, Ogoja, Yakuur, Yala"));
            stateToLocalGovtMap.put("Delta", parseToList(
                    "Aniocha North, Oniocha South, Burutu, Ethiope, Ethiope East, Ethiope West, Ika North East, Ika South, Isoko, Isoko North, Isoko South, Ndokwa East, Ndokwa West, Oshimili North, Oshimili South, Okpe, Sapele, Ughelli North, Ughelli South, Ukwuani, Uvbie, Warri North, Warri South, Warri South West"));
            stateToLocalGovtMap.put("Ebonyi", parseToList(
                    "Abakaliki, Afikpo North, Afikpo South, Ebonyi, Ezza North, Ezza South, Ikwo, Ishielu, Ivo, Izzi, Ohaozara, Ohaukwu, Onicha"));
            stateToLocalGovtMap.put("Enugu", parseToList(
                    "Aninri, Awgu, Enugu East, Enugu North, Enugu South, Ezeagu, Igbo Etiti, Igbo Eze North, Igbo Eze South, Isi Uzo, Nkanu East, Nkanu West, Nsukka, Oji River, Udenu, Udi, Uzo-Uwani"));
            stateToLocalGovtMap.put("Edo", parseToList(
                    "Akoko-Edo, Egor, Esan Central, Esan North-East, Esan South-East, Esan West, Etsako Central, Etsako East, Etsako West, Igueben, Ikpoba-Okha, Oredo, Orhionmwon, Ovia North-East, Ovia South-West, Owan East, Owan West, Uhunmwonde"));
            stateToLocalGovtMap.put("Ekiti", parseToList(
                    "Ado-Ekiti, Ikere, Oye, Aiyekire (Gbonyin), Efon, Ekiti East, Ekiti South-West, Ekiti West, Emure, Ido-Osi, Ijero, Ikole, Ilejemeje, Irepodun/Ifelodun, Ise/Orun, Moba"));
            stateToLocalGovtMap.put("Gombe", parseToList(
                    "Akko, Alkakeri, Balanga, Billiri, Dukku, Funakaye, Kaltungo, Kwami, Nafada-Bajoga, Shongom, Yamaltu-Deba"));
            stateToLocalGovtMap.put("Imo", parseToList(
                    " Aboh Mbaise, Ahiazu Mbaise, Ehime Mbano, Ezinihitte Mbaise, Ideato North, Ideato South, Ihitte/Uboma, Ikeduru, Isiala Mbano, Isu, Mbaitoli, Ngor Okpala, Njaba, Nkwerre, Nwangele, Obowo, Oguta, Ohaji/Egbema, Okigwe, Onuimo, Orlu, Orsu, Oru East, Oru West, Owerri Municipal, Owerri North, Owerri West"));
            stateToLocalGovtMap.put("Jigawa", parseToList(
                    "Auyo, Babura, Biriniwa, Birnin Kudu, Buji, Dutse, Gagarawa, Garki, Gumel, Guri, Gwaram, Gwiwa, Hadejia, Jahun, Kafin Hausa, Kaugama, Kazaure, Kiri Kasama, Kiyawa, Maigatari, Malam Madori, Miga, Ringim, Roni, Sule Tankarkar, Taura, Yankwashi"));
            stateToLocalGovtMap.put("Kaduna", parseToList(
                    "Birnin Gwari, Chikun, Giwa, Igabi, Ikara, Jaba, Jema'a, Kachia, Kaduna North, Kaduna South, Kagarko, Kajuru, Kaura, Kauru, Kubau, Kudan, Lere, Makarfi, Sabon Gari, Sanga, Soba, Zangon Kataf, Zaria"));
            stateToLocalGovtMap.put("Kano", parseToList(
                    "Ajingi, Albasu, Bagwai, Bebeji, Bichi, Bunkure, Dala, Dambatta, Dawakin Kudu, Dawakin Tofa, Doguwa, Fagge, Gabasawa, Garko, Garun Mallam, Gaya, Gezawa, Gwale, Gwarzo, Kabo, Kano Metropolitan Area, Kano Municipal, Karaye, Kibiya, Kiru, Kumbotso, Kunchi, Kura, Madobi, Makoda, Minjibir, Nassarawa, Northern Kano State, Rano, Rimin Gado, Rogo, Shanono, Southern Kano State, Sumaila, Takai, Tarauni, Tofa, Tsanyawa, Tudun Wada, Ungogo, Warawa, Wudil"));
            stateToLocalGovtMap.put("Katsina", parseToList(
                    "Bakori, Batagarawa, Batsari, Baure, Bindawa, Charanchi, Dan Musa, Dandume, Danja, Daura, Dutsi, Dutsin-Ma, Faskari, Funtua, Ingawa, Jibia, Kafur, Kaita, Kankara, Kankia, Katsina, Kurfi, Kusada, Mai'Adua, Malumfashi, Mani, Mashi, Matazu, Musawa, Rimi, Sabuwa, Safana, Sandamu, Zango "));
            stateToLocalGovtMap.put("Kebbi", parseToList(
                    "Aleiro, Arewa Dandi, Argungu, Augie, Bagudo, Birnin Kebbi, Bunza, Dandi, Fakai, Gwandu, Jega, Kalgo, Koko/Besse, Maiyama, Ngaski, Sakaba, Shanga, Suru, Danko/Wasagu, Yauri, Zuru"));
            stateToLocalGovtMap.put("Kogi", parseToList(
                    "Adavi, Ajaokuta, Ankpa, Bassa, Dekina, Ibaji, Idah, Igalamela-Odolu, Ijumu, Kabba/Bunu, Koton Karfe, Lokoja, Mopa-Muro, Ofu, Ogori/Magongo, Okehi, Okene, Olamaboro, Omala, Yagba East, Yagba West"));
            stateToLocalGovtMap.put("Kwara", parseToList(
                    "Asa, Baruten, Edu, Ekiti, Ifelodun, Ilorin East, Ilorin South, Ilorin West, Irepodun, Isin, Kaiama, Moro, Offa, Oke Ero, Oyun, Pategi"));
            stateToLocalGovtMap.put("Lagos", parseToList(
                    "Agege, Ajeromi-Ifelodun, Alimosho, Amuwo-Odofin, Apapa, Badagry, Badagry Division, Epe, Epe Division, Eti-Osa, Ibeju-Lekki, Ifako-Ijaye, Ikeja, Ikeja Division, Ikorodu, Ikorodu Division, Kosofe, Lagos Division, Lagos Island, Lagos Mainland, Mushin, Ojo, Oshodi-Isolo, Shomolu, Surulere"));
            stateToLocalGovtMap.put("Nasarawa", parseToList(
                    "Akwanga, Awe, Doma, Karu, Keana, Keffi, Kokona, Lafia, Nasarawa, Nasarawa-Eggon, Obi, Toto, Wamba"));
            stateToLocalGovtMap.put("Niger", parseToList(
                    "Agaie, Agwara, Bida, Borgu, Bosso, Chanchaga, Edati, Gbako, Gurara, Katcha, Kontagora, Lapai, Lavun, Magama, Mariga, Mashegu, Mokwa, Munya, Paikoro, Rafi, Rijau, Shiroro, Suleja, Tafa, Wushishi"));
            stateToLocalGovtMap.put("Ogun", parseToList(
                    "Abeokuta North, Abeokuta South, Ado-Odo/Ota, Ewekoro, Ifo, Ijebu East, Ijebu North, Ijebu North East, Ijebu Ode, Ikenne, Imeko Afon, Ipokia, Obafemi Owode, Odogbolu, Odeda, Ogun Waterside, Remo North, Sagamu, (Shagamu), Yewa North (formerly Egbado North), Yewa South (formerly Egbado South)"));
            stateToLocalGovtMap.put("Ondo", parseToList(
                    "Akoko North-East, Akoko North-West, Akoko South-East, Akoko South-West, Akure North, Akure South, Ese Odo, Idanre, Ifedore, Ilaje, Ile Oluji/Okeigbo, Irele, Odigbo, Okitipupa, Ondo East, Ondo West, Ose, Owo"));
            stateToLocalGovtMap.put("Osun", parseToList(
                    "Aiyedaade, Aiyedire, Atakunmosa East, Atakunmosa West, Boluwaduro, Boripe, Ede North, Ede South, Egbedore, Ejigbo, Ife Central, Ife East, Ife North, Ife South, Ifedayo, Ifelodun, Ila, Ilesa East, Ilesa West, Irepodun, Irewole, Isokan, Iwo, Obokun, Odo Otin, Ola Oluwa, Olorunda, Oriade, Orolu, Osogbo"));
            stateToLocalGovtMap.put("Oyo", parseToList(
                    "Afijio Jobele, Akinyele Moniya, Egbeda Egbeda, Ibadan North Agodi Gate, Ibadan North-East Iwo Road, Ibadan North-West Dugbe/Onireke, Ibadan South-West Ring Road, Ibadan South-East Mapo, Ibarapa Central Igbo Ora, Ibarapa East Eruwa, Ido Ido, Irepo Kisi-Okeogun, Iseyin Iseyin-Okeogun, Kajola Okeho-Okeogun, Lagelu Iyanaofa, Ogbomosho North Ogbomoso, Ogbomosho South Arowomole, Oyo West Ojongbodu, Atiba Ofa Meta, Atisbo Tede-Okeogun, Saki West Shaki-Okeogun, Saki East Agoamodu-Okeogun, Itesiwaju Otu-Okeogun, Iwajowa Iwereile-Okeogun, Ibarapa North Ayete, Olorunsogo Igbeti-Okeogun, Oluyole Idi Ayunre, Ogo Oluwa Ajawa, Surulere Iresa Adu, Orelope Igboho-Okeogun, Ori Ire Ikoyi, Oyo East Kosobo, Ona Ara Akanran"));
            stateToLocalGovtMap.put("Plateau", parseToList(
                    "Barkin Ladi, Bassa, Bokkos, Jos East, Jos North, Jos South, Kanam, Kanke, Langtang North, Langtang South, Mangu, Mikang, Pankshin, Qua'an Pan, Riyom, Shendam, Wase"));
            stateToLocalGovtMap.put("Rivers", parseToList(
                    "Santa Barbara River, Bonny River, Andoni River, New Calabar River, Nun River, River Orashi, San Bartholomeo River, Sombreiro River, St Nicholas River, Otamiriochie River, Ogochie River, Imo Miriochie River, Oloshi River, Opobo Channel River"));
            stateToLocalGovtMap.put("Sokoto", parseToList(
                    "Binji, Bodinga, Dange Shuni, Gada, Goronyo, Gudu, Gwadabawa, Illela, Isa, Kebbe, Kware, Rabah, Sabon Birni, Shagari, Silame, Sokoto North, Sokoto South, Tambuwal, Tangaza, Tureta, Wamako, Wurno, Yabo"));
            stateToLocalGovtMap.put("Yobe", parseToList(
                    "Bade, Bursari, Damaturu, Geidam, Gujba, Gulani, Fika, Fune, Jakusko, Karasuwa, Machina, Nangere, Nguru, Potiskum, Tarmuwa, Yunusari, Yusufari"));
            stateToLocalGovtMap.put("Zamfara", parseToList(
                    "Anka, Bakura, Birnin Magaji/Kiyaw, Bukkuyum, Bungudu, Chafe (Tsafe), Gummi, Gusau, Kaura Namoda, Maradun, Maru, Shinkafi, Talata Mafara, Zurmi"));
            stateToLocalGovtMap.put("FCT", parseToList("Abaji, Abuja, Bwari, Gwagwalada, Kuje, Kwali"));

            for (Map.Entry<String, List<String>> entry : stateToLocalGovtMap.entrySet()) {
                State stateDto = new State();
                stateDto.setName(entry.getKey());
                stateDto.setCode(entry.getKey().substring(0,3).toUpperCase());
                stateDto.setLocalGovts(new ArrayList<>(entry.getValue()));
                if (!stateRepo.existsByNameIgnoreCase(stateDto.getName()))
                    stateRepo.save(stateDto);
            }
        }
    }

    private List<String> parseToList(String categories) {
        String[] categoryArray = categories.split(",");
        return Arrays.asList(categoryArray);
    }

    private void viewTicketTillSummary(){
        List<TicketTillView> ticketTillViewSummaryList = ticketTillRepo.findAggregatedTicketTillByIssuanceDateAndStatus("2020-10-08",false);
        int counter =0;
        for (TicketTillView t:ticketTillViewSummaryList) {
            counter++;
        }
    }

}