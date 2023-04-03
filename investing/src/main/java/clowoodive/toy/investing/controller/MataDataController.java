//package clowoodive.toy.investing.controller;
//
//import clowoodive.toy.investing.service.DataService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "/metadata")
//public class MataDataController {
//    private final DataService dataService;
//
//    @Autowired
//    public MataDataController(DataService dataService) {
//        this.dataService = dataService;
//    }
//
//    @PostMapping("/init")
//    public void initMetaData() {
//        dataService.initProductMetaData();
//    }
//}
