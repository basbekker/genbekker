package org.bbekker.genealogy.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.AppConstants.Roles;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.EventRepository;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.repository.RelationshipRepository;
import org.bbekker.genealogy.repository.RoleType;
import org.bbekker.genealogy.util.IndividualFullView;
import org.bbekker.genealogy.util.RelationshipWithOther;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class IndividualServiceImpl implements IndividualService {

	@Value("${spring.data.web.pageable.default-page-size}")
	private int defaultPageSize;

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private RelationshipRepository relationshipRepository;

	@Autowired
	private RelationshipService relationshipService;

	private PageHandlerUtil<Individual> pageHandler;


	@Override
	public PageHandlerUtil<Individual> findAll() {

		pageHandler = new PageHandlerUtil<Individual>(individualRepository.findAll());
		return pageHandler;
	}

	@Override
	public PageHandlerUtil<Individual> findAllPaged(int currentPage) {

		Pageable newPage = PageRequest.of(currentPage, defaultPageSize);
		pageHandler = new PageHandlerUtil<Individual>(individualRepository.findAll(newPage));
		return pageHandler;
	}

	@Override
	public PageHandlerUtil<Individual> findLikePaged(String lastName, int currentPage) {

		Pageable newPage = PageRequest.of(currentPage, defaultPageSize);
		pageHandler = new PageHandlerUtil<Individual>(individualRepository.findByLastNameLike(lastName, newPage));
		return pageHandler;
	}

	@Override
	public Individual get(String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);
		if (optionalIndividual.isPresent()) {
			return optionalIndividual.get();
		}
		return null;
	}

	@Override
	public String getSelfUri(String id) {
		String baseURI = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		return baseURI + "/api/individual/" + id;
	}

	@Override
	public Individual save(Individual individual) {

		return individualRepository.save(individual);
	}

	@Override
	public void saveAll(List<Individual> individuals) {

		for (Individual individual : individuals) {
			save(individual);
		}
	}

	@Override
	public Individual delete(String id) {

		Optional<Individual> optionalIndividual = individualRepository.findById(id);
		if (optionalIndividual.isPresent()) {
			return delete(optionalIndividual.get());
		}
		return null;
	}

	@Override
	public Individual delete(Individual individual) {

		if (individual != null) {
			relationshipService.deleteRelationshipsForIndividual(individual);
			individualRepository.delete(individual);
		}

		individual = new Individual(SystemConstants.EMPTY_STRING, SystemConstants.EMPTY_STRING);
		individual.setId(AppConstants.ID_DELETED);

		return individual;
	}

	public Integer getPageSize() {
		return Integer.valueOf(defaultPageSize);
	}

	@Override
	public Integer getNumberOfElements() {
		Long elementCount = individualRepository.count();
		return elementCount.intValue();
	}

	@Override
	public IndividualFullView getFullView(String id) {
		IndividualFullView fullIndividual = new IndividualFullView(individualRepository, eventRepository, relationshipRepository);
		fullIndividual.setIndividual(id);
		return fullIndividual;
	}

	@Override
	public IndividualFullView getFullView(Individual individual) {
		IndividualFullView fullIndividual = new IndividualFullView(individualRepository, eventRepository, relationshipRepository);
		fullIndividual.setIndividual(individual);
		return fullIndividual;
	}

	@Override
	public List<Individual> getKids(Individual individual) {

		List<Individual> kids = new LinkedList<Individual>();

		IndividualFullView fullIndividual = new IndividualFullView(individualRepository, eventRepository, relationshipRepository);
		fullIndividual.setIndividual(individual);

		List<RelationshipWithOther> relations = fullIndividual.getRelationshipsWithOther();
		for (RelationshipWithOther relation : relations) {
			RoleType roleType = relation.getOtherRoleType();
			if ( roleType.getQualifier().equals(Roles.SON.getQualifier()) || roleType.getQualifier().equals(Roles.DAUGHTER.getQualifier()) ) {
				kids.add(relation.getOtherIndividual());
			}
		}

		return kids;
	}

	@Override
	public Individual getPartner(Individual individual) {

		Individual partner = null;

		if (individual != null) {
			IndividualFullView fullIndividual = new IndividualFullView(individualRepository, eventRepository, relationshipRepository);
			fullIndividual.setIndividual(individual);

			List<RelationshipWithOther> relations = fullIndividual.getRelationshipsWithOther();
			for (RelationshipWithOther relation : relations) {
				RoleType roleType = relation.getOtherRoleType();
				if ( roleType.getQualifier().equals(Roles.HUSBAND.getQualifier()) || roleType.getQualifier().equals(Roles.WIFE.getQualifier()) ) {
					partner = relation.getOtherIndividual();
				}
			}
		}

		return partner;
	}

}
