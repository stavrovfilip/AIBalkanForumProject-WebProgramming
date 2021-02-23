package com.webproject.aibalkanforumproject.web;

import com.webproject.aibalkanforumproject.model.*;
import com.webproject.aibalkanforumproject.service.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final CompanyService companyService;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    public JobController(JobService jobService, CompanyService companyService, LocationService locationService, CategoryService categoryService, ImageService imageService) {
        this.jobService = jobService;
        this.companyService = companyService;
        this.locationService = locationService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @GetMapping
    public String getJobsPage(Model model){
        List<Job> jobs = jobService.findAll();

        model.addAttribute("jobs",jobs);
        model.addAttribute("bodyContent","job-list");
        return "master-template";
    }

    @GetMapping("/info/{id}")
    public String getJobInfo(@PathVariable Long id, Model model){
        Job job = jobService.findById(id);
        model.addAttribute("job",job);
        model.addAttribute("bodyContent","job-info");
        return "master-template";
    }

    @GetMapping("/image/{id}")
    public void showProductImage(@PathVariable Long id,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("image/*");

        Job job = jobService.findById(id);
        InputStream is = new ByteArrayInputStream(job.getImage().getData());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping("/add-form")
    public String jobFormPage(Model model){
        List<Company> companies = companyService.findAll();
        List<Location> locations = locationService.findAll();
        List<Category> categories = categoryService.listCategories();
        List<JobType> jobTypes = List.of(JobType.FULL_TIME,JobType.PART_TIME,JobType.FREELANCE,JobType.REMOTE);
        model.addAttribute("jobTypes",jobTypes);
        model.addAttribute("categories",categories);
        model.addAttribute("companies",companies);
        model.addAttribute("locations",locations);
        model.addAttribute("bodyContent","jobs-form");
        return "master-template";
    }

    @PostMapping("/add")
    public String addJob(@RequestParam(required = false) Long id,
                         @RequestParam String companyId,
                         @RequestParam String title,
                         @RequestParam JobType jobType,
                         @RequestParam Long jobCategoryId,
                         @RequestParam Long jobLocationId,
                         @RequestParam String jobSalary,
                         @RequestParam String jobDescription,
                         @RequestParam String knowLedgeSkillsAndAbilities,
                         @RequestParam String educationAndExperience,
                         @RequestParam MultipartFile urlImage,
                         @RequestParam Date datepicker) throws IOException {
        Image image = null;
        if(id!=null){
            if(urlImage.getOriginalFilename().isEmpty()) {
                jobService.edit(id,
                        companyId,
                        title,
                        jobType,
                        jobDescription,
                        knowLedgeSkillsAndAbilities,
                        educationAndExperience,
                        jobSalary,
                        jobLocationId,
                        datepicker,
                        jobCategoryId,
                        null);
            }
            else{
                imageService.delete(jobService.findById(id).getImage().getId());
                image = imageService.store(urlImage);
                jobService.edit(id,
                        companyId,
                        title,
                        jobType,
                        jobDescription,
                        knowLedgeSkillsAndAbilities,
                        educationAndExperience,
                        jobSalary,
                        jobLocationId,
                        datepicker,
                        jobCategoryId,
                        image);
            }
        }
        else {
            image = imageService.store(urlImage);
            jobService.create(companyId,
                    title,
                    jobType,
                    jobDescription,
                    knowLedgeSkillsAndAbilities,
                    educationAndExperience,
                    jobSalary,
                    jobLocationId,
                    datepicker,
                    jobCategoryId,
                    image);
        }
        return "redirect:/jobs";
    }

    @GetMapping("/{id}/edit")
    public String editJob(@PathVariable Long id,Model model){
        Job job = jobService.findById(id);
        List<JobType> jobTypes = List.of(JobType.FULL_TIME,JobType.PART_TIME,JobType.FREELANCE,JobType.REMOTE);
        List<Company> companies = companyService.findAll();
        List<Location> locations = locationService.findAll();
        List<Category> categories = categoryService.listCategories();
        model.addAttribute("job",job);
        model.addAttribute("jobTypes",jobTypes);
        model.addAttribute("categories",categories);
        model.addAttribute("companies",companies);
        model.addAttribute("locations",locations);
        model.addAttribute("bodyContent","jobs-form");
        return "master-template";
    }

    @GetMapping("/{id}/delete")
    public String deleteJob(@PathVariable Long id){
        imageService.delete(jobService.findById(id).getImage().getId());
        jobService.delete(id);
        return "redirect:/jobs";
    }

}
