package com.market.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "storage_file_name")
    private String storageFileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column
    private long size;

    @Column
    private String type;

    @Column
    private String url;
}