package com.B305.ogym.domain.mappingTable;

//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "pt_student_monthly")
public class PTStudentMonthly {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "pt_student_monthly_id")
//    private Long id; // 대리 키
//
//    private int height; // 키
//    private int weight; // 몸무게
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pt_student_id")
//    private PTStudent ptStudent; // 학생
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "month")
//    private Monthly monthly; // 월
//
//    @Builder
//    public PTStudentMonthly(int height, int weight,PTStudent ptStudent, Monthly monthly){
//        this.height = height;
//        this.weight = weight;
//        this.ptStudent = ptStudent;
//        if(!ptStudent.getPtStudentMonthly().contains(this))
//            ptStudent.getPtStudentMonthly().add(this);
//        this.monthly = monthly;
//    }
//
//    public static PTStudentMonthly createHealth(int height, int weight,PTStudent ptStudent, Monthly monthly){
//        PTStudentMonthly result = PTStudentMonthly.builder()
//            .height(height)
//            .weight(weight)
//            .ptStudent(ptStudent)
//            .monthly(monthly)
//            .build();
//        if(!ptStudent.getPtStudentMonthly().contains(result)){
//            ptStudent.getPtStudentMonthly().add(result);
//        }
//        return result;
//    }
}
