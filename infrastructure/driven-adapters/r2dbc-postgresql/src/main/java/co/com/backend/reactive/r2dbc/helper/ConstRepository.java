package co.com.backend.reactive.r2dbc.helper;


public final class ConstRepository {
    private ConstRepository() {}

    public static final String FIND_TECHNOLOGY_IDS_BY_CAPACITY_ID =
            "SELECT technology_id FROM capacity_technology WHERE capacity_id = :capacityId";

    public static final String COUNT_CAPACITIES_BY_TECHNOLOGY_ID =
            "SELECT COUNT(*) FROM capacity_technology WHERE technology_id = :technologyId";

    public static final String DELETE_BY_CAPACITY_IDS =
            "DELETE FROM capacity_technology WHERE capacity_id IN (:capacityIds)";

    public static final String FIND_ALL_PAGINATED =
            "SELECT c.id, c.name, c.description, " +
                    "COUNT(ct.technology_id) as tech_count " +
                    "FROM capacities c " +
                    "LEFT JOIN capacity_technology ct ON c.id = ct.capacity_id " +
                    "GROUP BY c.id, c.name, c.description " +
                    "ORDER BY " +
                    "CASE WHEN :sortBy = 'name' AND :sortDirection = 'asc' THEN c.name ELSE NULL END ASC, " +
                    "CASE WHEN :sortBy = 'name' AND :sortDirection = 'desc' THEN c.name ELSE NULL END DESC, " +
                    "CASE WHEN :sortBy = 'tech_count' AND :sortDirection = 'asc' THEN COUNT(ct.technology_id) ELSE NULL END ASC, " +
                    "CASE WHEN :sortBy = 'tech_count' AND :sortDirection = 'desc' THEN COUNT(ct.technology_id) ELSE NULL END DESC " +
                    "LIMIT :size OFFSET :offset";
}
